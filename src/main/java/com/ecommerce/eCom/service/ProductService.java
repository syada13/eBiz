package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Product;
import com.ecommerce.eCom.payload.ProductDTO;
import com.ecommerce.eCom.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
    ProductResponse getAllProducts();
    ProductResponse searchProductsByCategory(Long categoryId);
    ProductResponse searchProductsByKeyword(String keyword);
    ProductDTO updateProduct(Long productId,ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
