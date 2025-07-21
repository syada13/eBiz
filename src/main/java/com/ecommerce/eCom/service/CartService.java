package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Cart;
import com.ecommerce.eCom.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
    List<CartDTO> getAllCarts();
    CartDTO getCart(String emailId, Long cartId);
}
