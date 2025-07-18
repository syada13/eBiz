package com.ecommerce.eCom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id" )
    private Cart cart;

    @ManyToOne
    @JoinColumn(name ="product_id")
    private Product product;

    private Integer quantity;
    private double discount;
    private double productPrice;
}
