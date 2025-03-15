package com.ecommerce.eCom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private String description;
    private Integer quantity;
    private double price;
    private double specialPrice;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

}
