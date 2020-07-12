package com.odeal.vendingmachine.payments.services;

import com.odeal.vendingmachine.payments.dtos.PaymentRequest;

public abstract class PaymentServiceAbstract {

    protected Double getTotalPrice(PaymentRequest paymentRequest){
        return paymentRequest.getProduct().getPrice()*paymentRequest.getAmount();
    }
}
