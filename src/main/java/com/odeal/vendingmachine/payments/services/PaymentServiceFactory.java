package com.odeal.vendingmachine.payments.services;

import com.odeal.vendingmachine.payments.exceptions.PaymentServiceNotFoundException;
import com.odeal.vendingmachine.payments.dtos.PaymentType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceFactory {

    private final List<PaymentService> paymentServices;

    public PaymentService getPaymentService(PaymentType paymentType){
        return paymentServices.stream()
                .filter(paymentService -> paymentService.getType() == paymentType)
                .findAny()
                .orElseThrow(PaymentServiceNotFoundException::new);
    }

    public PaymentServiceFactory(List<PaymentService> paymentServices) {
        this.paymentServices = paymentServices;
    }
}
