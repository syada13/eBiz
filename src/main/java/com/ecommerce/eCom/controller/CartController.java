package com.ecommerce.eCom.controller;

import com.ecommerce.eCom.payload.CartDTO;
import com.ecommerce.eCom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {


    @Autowired
    private CartService cartService;

    @PostMapping("/carts/product/{productId}/quantity/{quantity}")
    private ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                     @PathVariable Integer quantity){

        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    private ResponseEntity<List<CartDTO>> getAllCarts(){
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOs,HttpStatus.FOUND);
    }
}
