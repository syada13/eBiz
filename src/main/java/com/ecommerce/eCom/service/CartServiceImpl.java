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
                          .map(p -> modelMapper.map(p.getProduct(),ProductDTO.class))
                          .toList();
                  cartDTO.setProducts(products);
                  return cartDTO;
                }).toList();
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
