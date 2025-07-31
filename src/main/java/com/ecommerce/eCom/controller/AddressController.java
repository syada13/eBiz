package com.ecommerce.eCom.controller;


import com.ecommerce.eCom.model.User;
import com.ecommerce.eCom.payload.AddressDTO;
import com.ecommerce.eCom.repositories.AddressRepository;
import com.ecommerce.eCom.service.AddressService;
import com.ecommerce.eCom.util.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtils authUtils;


    @Autowired
    AddressService addressService;

    @PostMapping("/addresses")
    private ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtils.loggedInuser();
        AddressDTO createdAddressDTO = addressService.createAddress(addressDTO,user);
        return new ResponseEntity<AddressDTO>(createdAddressDTO, HttpStatus.CREATED);
    }
}
