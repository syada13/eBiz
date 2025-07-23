package com.ecommerce.eCom.service;

import com.ecommerce.eCom.exceptions.APIException;
import com.ecommerce.eCom.exceptions.ResourceNotFoundException;
import com.ecommerce.eCom.model.Cart;
import com.ecommerce.eCom.model.CartItem;
import com.ecommerce.eCom.model.Product;
import com.ecommerce.eCom.payload.CartDTO;
import com.ecommerce.eCom.payload.ProductDTO;
import com.ecommerce.eCom.repositories.CartItemRepository;
import com.ecommerce.eCom.repositories.CartRepository;
import com.ecommerce.eCom.repositories.ProductRepository;
import com.ecommerce.eCom.util.AuthUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    AuthUtils authUtil;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = findOrCreateCart();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));


        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(),
                productId
        );

        if (cartItem !=null){
            throw new APIException("Product "+ product.getProductName() + " already exists in cart.");
        }

        if (product.getQuantity() == 0){
            throw new APIException(product.getProductName() + "is currently not available.");
        }

        if (product.getQuantity() < quantity){
            throw new APIException( "Please make an order of "+ product.getProductName() +
                    "less than or equal to the quantity" +product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        // Reduce stock ???
        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item, ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) {
            throw new APIException("No cart exists.");
        }

        return carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
                  List<ProductDTO> products = cart.getCartItems().stream()
                          .map(p -> {
                              ProductDTO map = modelMapper.map(p.getProduct(),ProductDTO.class);
                              map.setQuantity(p.getQuantity());
                              return map;
                          }).toList();

                  cartDTO.setProducts(products);
                  return cartDTO;
                }).toList();
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart =  cartRepository.findCartByEmailAndCartId(emailId,cartId);

        if(cart == null ){
            throw new ResourceNotFoundException("Cart ","cartId",cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<ProductDTO> products = cart.getCartItems().stream()
                .map(item -> {
                            ProductDTO mappedProductDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                            mappedProductDTO.setQuantity(item.getQuantity());
                            return mappedProductDTO;
                }).toList();

        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityToCart(Long productId, Integer quantity) {

        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        Long cartId = userCart.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart ","cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        //Validations Before updating product quantity
        if (product.getQuantity() == 0){
            throw new APIException(product.getProductName() + "is currently not available.");
        }

        if (product.getQuantity() < quantity){
            throw new APIException( "Please make an order of "+ product.getProductName() +
                    "less than or equal to the quantity" +product.getQuantity());
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if (cartItem == null ){
            throw new APIException("Product: "+ product.getProductName() + " not found in the user cart");
        }


        //Update product quantity
        cartItem.setQuantity(product.getQuantity() + quantity);
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
        cartRepository.save(cart);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        /*
        Cart has only one product/cartItem, AND operation is to reduce quantity of the product by 1.
        Remove the cartItem/product from the cart
         */
        if (updatedCartItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }


        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStreams = cartItems.stream().map(item ->{
            ProductDTO prd = modelMapper.map(item,ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });

        cartDTO.setProducts(productStreams.toList());
        return cartDTO;
    }

    private Cart findOrCreateCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart !=null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInuser());
        return cartRepository.save(cart);
    }
}
