package com.ecommerce.eCom.payload;

import com.ecommerce.eCom.model.Address;
import com.ecommerce.eCom.model.OrderItem;
import com.ecommerce.eCom.model.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private List<OrderItemDTO> orderItems;
    private String email;
    private LocalDate orderDate;
    private Double totalAmount;
    private String orderStatus;
    private Address address;
    private PaymentDTO payment;

}
