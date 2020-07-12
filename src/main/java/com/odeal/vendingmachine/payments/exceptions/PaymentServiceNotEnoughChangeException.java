package com.odeal.vendingmachine.payments.exceptions;

import com.odeal.vendingmachine.errors.ErrorRuntimeException;
import com.odeal.vendingmachine.payments.dtos.Money;

import java.util.List;

public class PaymentServiceNotEnoughChangeException extends ErrorRuntimeException {

    public PaymentServiceNotEnoughChangeException(List<Money> moneyList) {
        super("Inventory of Money change is not enough for payment");
        setMoneyList(moneyList);
    }

    public PaymentServiceNotEnoughChangeException() {
    }
}
