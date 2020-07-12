package com.odeal.vendingmachine.orders.controllers.responses;

import com.odeal.vendingmachine.payments.dtos.Money;
import com.odeal.vendingmachine.payments.dtos.PaymentType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse implements Serializable {

    private String productName;
    private Integer productAmount;
    private PaymentType paymentType;
    private Double totalPrice;
    private Double change;
    private List<Money> changes;
    private String errorMessage;

}
