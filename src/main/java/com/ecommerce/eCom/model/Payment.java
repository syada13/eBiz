package com.ecommerce.eCom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Order order;

    @NotBlank
    @Size(min=5, message= "Payment method must contain at least 4 characters.")
    private String paymentMethod;

    private String pgPaymentId;
    private String pgName;
    private String pgStatus;
    private String pgResponseMessage;

    /*
     Custom constructor to create Payment class object with all information we get from payment gateway
     without Order information and connect to Order object.
     */
    public Payment(String paymentMethod, String pgPaymentId,String pgName, String pgStatus, String pgResponseMessage){
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgName = pgName;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
    }
}
