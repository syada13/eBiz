package com.ecommerce.eCom.service;

import com.ecommerce.eCom.exceptions.APIException;
import com.ecommerce.eCom.exceptions.ResourceNotFoundException;
import com.ecommerce.eCom.model.Cart;
import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.model.Product;
import com.ecommerce.eCom.payload.CartDTO;
import com.ecommerce.eCom.payload.ProductDTO;
import com.ecommerce.eCom.payload.ProductResponse;
import com.ecommerce.eCom.repositories.CartRepository;
import com.ecommerce.eCom.repositories.CategoryRepository;
import com.ecommerce.eCom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();

        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() -
                    ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw new APIException("Product already exist!!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize,String sortBy, String sortOrder) {

        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
       Pageable pageDetails =  PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
       Page<Product> productPage = productRepository.findAll(pageDetails);
       List<Product> products = productPage.getContent();
       if(products.isEmpty()){
           throw new APIException("No products created yet.");
       }
      List<ProductDTO> productDTOs = products.stream()
              .map(product -> modelMapper.map(product, ProductDTO.class))
              .toList();

      ProductResponse productResponse = new ProductResponse();
      productResponse.setContent(productDTOs);
      productResponse.setPageNumber(productPage.getNumber());
      productResponse.setPageSize(productPage.getSize());
      productResponse.setTotalElements(productPage.getTotalElements());
      productResponse.setTotalPages(productPage.getTotalPages());
      productResponse.setLastPage(productPage.isLast());
      return productResponse;
    }


    @Override
    public ProductResponse searchProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow( ()->
                                new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails =  PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        //Page<Product> productPage = productRepository.findAll(pageDetails);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> products = productPage.getContent();
        if(products.isEmpty()){
            throw new APIException("No products found for category: "+category.getCategoryName());
        }

        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    public ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails =  PageRequest.of(pageNumber,pageSize,sortByAndSortOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%',pageDetails);
        List<Product> products = productPage.getContent();
        if(products.isEmpty()){
            throw new APIException("No products found with given keyword: "+keyword);
        }
        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        productResponse.setContent(productsDTOs);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    public ProductDTO updateProduct(Long productId,ProductDTO productDTO){
       Product productFromDB = productRepository.findById(productId)
               .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

       Product product = modelMapper.map(productDTO,Product.class);

        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01) * product.getPrice());
        productFromDB.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(productFromDB);
        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart ->{
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p-> modelMapper.map(p.getProduct(),ProductDTO.class))
                    .toList();
            cartDTO.setProducts(products);
            return cartDTO;
        }).toList();

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(),productId));
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

  public ProductDTO deleteProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

      List<Cart> carts = cartRepository.findCartsByProductId(productId);
      carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(),productId));

      productRepository.delete(product);
      return  modelMapper.map(product,ProductDTO.class);
  }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        String fileName = fileService.uploadImage(path,image);
        productFromDB.setImage(fileName);
        Product updatedProduct= productRepository.save(productFromDB);
       return  modelMapper.map(updatedProduct,ProductDTO.class);
    }
}
