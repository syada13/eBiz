package com.ecommerce.eCom.repositories;

import com.ecommerce.eCom.model.Address;
import com.ecommerce.eCom.model.User;
import com.ecommerce.eCom.payload.AddressDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
    AddressDTO createAddress(AddressDTO addressDTO, User user);
}
