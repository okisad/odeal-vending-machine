package com.odeal.vendingmachine.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResult {

    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private Double paymentAmount;
    private Double change;
    private List<Money> changeMoneys;

}
