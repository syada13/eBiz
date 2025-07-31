package com.ecommerce.eCom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message =" Street must be atleast 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message =" Building name must be atleast 5 characters")
    private String building;

    @NotBlank
    @Size(min = 4, message =" City name must be atleast 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message =" Country name must be atleast 4 characters")
    private String country;

    public Address(String street, String building, String city, String country, String zipcode) {
        this.street = street;
        this.building = building;
        this.city = city;
        this.country = country;
        this.zipcode = zipcode;
    }

    @NotBlank
    @Size(min = 6, message =" Zipcode name must be atleast 6 characters")
    private String zipcode;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


}
