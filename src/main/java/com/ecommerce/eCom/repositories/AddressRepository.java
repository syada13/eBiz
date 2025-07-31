package com.ecommerce.eCom.repositories;

import com.ecommerce.eCom.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
