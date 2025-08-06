package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.User;
import com.ecommerce.eCom.payload.AddressDTO;

import java.util.List;


public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();
}
