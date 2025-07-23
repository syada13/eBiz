package com.ecommerce.eCom.service;


import com.ecommerce.eCom.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityToCart(Long productId, Integer operation);

    String deleteProductFromCart(Long cartId, Long productId);
}
