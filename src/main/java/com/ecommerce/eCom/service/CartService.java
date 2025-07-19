package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Cart;
import com.ecommerce.eCom.payload.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
}
