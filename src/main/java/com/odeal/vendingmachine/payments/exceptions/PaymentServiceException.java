package com.odeal.vendingmachine.payments.exceptions;

import com.odeal.vendingmachine.errors.ErrorRuntimeException;
import com.odeal.vendingmachine.payments.dtos.Money;

import java.util.List;

public class PaymentServiceException extends ErrorRuntimeException {



    public PaymentServiceException(List<Money> moneyList, String message) {
        super(message);
        setMoneyList(moneyList);
    }

}
