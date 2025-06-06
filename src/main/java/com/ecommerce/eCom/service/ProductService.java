package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Product;
import com.ecommerce.eCom.payload.ProductDTO;
import com.ecommerce.eCom.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
    ProductResponse getAllProducts();
    ProductResponse searchProductsByCategory(Long categoryId);
    ProductResponse searchProductsByKeyword(String keyword);
    ProductDTO updateProduct(Long productId,ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
}
