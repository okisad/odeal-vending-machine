package com.odeal.vendingmachine.payments.exceptions;

import com.odeal.vendingmachine.errors.ErrorRuntimeException;
import com.odeal.vendingmachine.payments.dtos.Money;

import java.util.List;

public class PaymentServiceMoneyNotEnoughException extends ErrorRuntimeException {

    public PaymentServiceMoneyNotEnoughException(List<Money> moneyList) {
        super("Money is not enough for payment");
        setMoneyList(moneyList);
    }

    public PaymentServiceMoneyNotEnoughException() {
    }
}
