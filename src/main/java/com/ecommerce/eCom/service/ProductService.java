package com.ecommerce.eCom.service;

import com.ecommerce.eCom.payload.ProductDTO;
import com.ecommerce.eCom.payload.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
    ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    ProductResponse searchProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductDTO updateProduct(Long productId,ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
