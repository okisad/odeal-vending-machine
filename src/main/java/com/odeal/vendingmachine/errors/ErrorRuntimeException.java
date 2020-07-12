package com.odeal.vendingmachine.errors;

import com.odeal.vendingmachine.payments.dtos.Money;

import java.util.List;

public abstract class ErrorRuntimeException extends RuntimeException{

    private List<Money> moneyList;

    public ErrorRuntimeException() {
    }

    public ErrorRuntimeException(String message) {
        super(message);
    }

    public ErrorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorRuntimeException(Throwable cause) {
        super(cause);
    }

    public ErrorRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public List<Money> getMoneyList() {
        return this.moneyList;
    }

    public void setMoneyList(List<Money> moneyList) {
        this.moneyList = moneyList;
    }
}
