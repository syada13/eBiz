package com.ecommerce.eCom.controller;

import com.ecommerce.eCom.model.Cart;
import com.ecommerce.eCom.payload.CartDTO;
import com.ecommerce.eCom.repositories.CartRepository;
import com.ecommerce.eCom.service.CartService;
import com.ecommerce.eCom.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {


    @Autowired
    AuthUtils authUtil;

    @Autowired
    CartRepository cartRepository;

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


    @GetMapping("/carts/users/cart")
    private ResponseEntity<CartDTO> getCartById(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId,cartId);
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }

    @PutMapping("/carts/products/{productId/quantity/{operation}")
    private ResponseEntity<CartDTO> updateProductQuantity(@PathVariable Long productId,
                                                          @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityToCart(productId,operation.equalsIgnoreCase("delete") ? -1: 1);
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }
}
