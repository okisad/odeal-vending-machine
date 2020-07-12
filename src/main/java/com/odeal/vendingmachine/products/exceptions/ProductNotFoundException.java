package com.odeal.vendingmachine.products.exceptions;

import com.odeal.vendingmachine.errors.ErrorRuntimeException;

public class ProductNotFoundException extends ErrorRuntimeException {


    public ProductNotFoundException() {

        super("Product is not found");
    }
}
