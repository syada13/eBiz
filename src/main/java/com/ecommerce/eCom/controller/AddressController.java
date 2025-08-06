package com.ecommerce.eCom.controller;


import com.ecommerce.eCom.model.Address;
import com.ecommerce.eCom.model.User;
import com.ecommerce.eCom.payload.AddressDTO;
import com.ecommerce.eCom.payload.ProductDTO;
import com.ecommerce.eCom.repositories.AddressRepository;
import com.ecommerce.eCom.service.AddressService;
import com.ecommerce.eCom.util.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("/addresses")
    private ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addressList = addressService.getAllAddresses();
        return new ResponseEntity<List<AddressDTO>>(addressList,HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    private ResponseEntity<AddressDTO> geAddressById(@PathVariable Long addressId) {
        AddressDTO address = addressService.getAddressById(addressId);
        return new ResponseEntity<AddressDTO>(address,HttpStatus.OK);
    }

    @GetMapping("/user/addresses")
    private ResponseEntity<List<AddressDTO>> getUserAddresses() {
        User user = authUtils.loggedInuser();
        List<AddressDTO> userAddressesList= addressService.getUserAddresses(user);
        return new ResponseEntity<List<AddressDTO>>(userAddressesList,HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    private ResponseEntity<AddressDTO> updateAddress(@Valid @RequestBody AddressDTO addressDTO,
                                                     @PathVariable Long addressId){
        AddressDTO updatedAddress = addressService.updateAddress(addressDTO,addressId);
        return new ResponseEntity<AddressDTO>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    private ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String status  = addressService.deleteAddress(addressId);
        return new ResponseEntity<String>(status,HttpStatus.OK);
    }

}
