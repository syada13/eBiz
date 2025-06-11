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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
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

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        // Get product from DB which image needs to be updated
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        // Upload image file to server
        // Get the file name of the uploaded image
        String path = "images/";
        String fileName = uploadImage(path,image);

        // Update filename to the product
        productFromDB.setImage(fileName);

        // Save product image
        Product updatedProduct= productRepository.save(productFromDB);

        // Return ProductDTO
       return  modelMapper.map(updatedProduct,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {

        // Original file name
        String originalFileName = file.getOriginalFilename();

        // Generate unique file name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        // Check if path exist and create
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdir();

        // Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Returning file name
        return fileName;
    }
}
