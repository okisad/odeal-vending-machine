package com.odeal.vendingmachine.inventories.exceptions;

import com.odeal.vendingmachine.errors.ErrorRuntimeException;

public class InventoryNotFoundException extends ErrorRuntimeException {

    public InventoryNotFoundException() {
        super("inventory is not found");
    }
}
