package com.odeal.vendingmachine.payments.exceptions;

import com.odeal.vendingmachine.errors.ErrorRuntimeException;
import com.odeal.vendingmachine.payments.dtos.Money;

import java.util.List;

public class PaymentServiceNotFoundException extends ErrorRuntimeException {

    public PaymentServiceNotFoundException(List<Money> moneyList) {
        super("payment service is not found");
        setMoneyList(moneyList);
    }

    public PaymentServiceNotFoundException() {
    }
}
