package com.odeal.vendingmachine.inventories.exceptions;

import com.odeal.vendingmachine.errors.ErrorRuntimeException;
import com.odeal.vendingmachine.payments.dtos.Money;

import java.util.List;

public class InventorySugarNotSufficientException extends ErrorRuntimeException {

    public InventorySugarNotSufficientException(List<Money> moneyList) {
        super("Sugar is not enough");
        setMoneyList(moneyList);
    }

    public InventorySugarNotSufficientException(String message,List<Money> moneyList) {
        super(message);
        setMoneyList(moneyList);
    }
}
