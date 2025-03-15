package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Product;
import com.ecommerce.eCom.payload.ProductDTO;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);
}
