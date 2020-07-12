package com.odeal.vendingmachine.inventories.exceptions;

import com.odeal.vendingmachine.errors.ErrorRuntimeException;
import com.odeal.vendingmachine.payments.dtos.Money;

import java.util.List;

public class InventoryProductNotSufficientException extends ErrorRuntimeException {

    public InventoryProductNotSufficientException() {
        super("Product count is not sufficent");
    }

    public InventoryProductNotSufficientException(List<Money> moneyList) {
        super("Product count is not sufficent");
        setMoneyList(moneyList);
    }

}
