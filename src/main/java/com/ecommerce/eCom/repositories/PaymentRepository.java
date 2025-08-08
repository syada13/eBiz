package com.ecommerce.eCom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.eCom.model.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
