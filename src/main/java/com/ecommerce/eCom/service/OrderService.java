package com.ecommerce.eCom.service;

import com.ecommerce.eCom.payload.OrderDTO;

public interface OrderService{
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
