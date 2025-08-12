package com.ecommerce.eCom.controller;


import com.ecommerce.eCom.model.Order;
import com.ecommerce.eCom.payload.OrderDTO;
import com.ecommerce.eCom.payload.OrderRequestDTO;
import com.ecommerce.eCom.service.OrderService;
import com.ecommerce.eCom.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private AuthUtils authUtil;

    @Autowired
    private OrderService orderService;


    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO){
        String emailId = authUtil.loggedInEmail();
        OrderDTO order = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );

        return new ResponseEntity<OrderDTO>(order, HttpStatus.CREATED);

    }
}
