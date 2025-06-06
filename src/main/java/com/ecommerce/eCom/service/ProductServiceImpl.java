package com.ecommerce.eCom.service;

import com.ecommerce.eCom.exceptions.ResourceNotFoundException;
import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.model.Product;
import com.ecommerce.eCom.payload.ProductDTO;
import com.ecommerce.eCom.payload.ProductResponse;
import com.ecommerce.eCom.repositories.CategoryRepository;
import com.ecommerce.eCom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        Product product = modelMapper.map(productDTO,Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
       Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
      List<Product> products = productRepository.findAll();
      List<ProductDTO> productDTOs = products.stream()
              .map(product -> modelMapper.map(product, ProductDTO.class))
              .toList();

      ProductResponse productResponse = new ProductResponse();
      productResponse.setContent(productDTOs);
      return productResponse;
    }


    @Override
    public ProductResponse searchProductsByCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow( ()->
                                new ResourceNotFoundException("Category","categoryId",categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        return productResponse;
    }

    public ProductResponse searchProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
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
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

  public ProductDTO deleteProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        productRepository.delete(product);
        return  modelMapper.map(product,ProductDTO.class);
  }
}
