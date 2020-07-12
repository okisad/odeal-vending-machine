package com.odeal.vendingmachine.payments.services;

import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.orders.controllers.requests.OrderRequest;
import com.odeal.vendingmachine.payments.dtos.PaymentRequest;
import com.odeal.vendingmachine.payments.dtos.PaymentResult;
import com.odeal.vendingmachine.payments.dtos.PaymentType;
import com.odeal.vendingmachine.products.dtos.Product;

public interface PaymentService {

    PaymentResult pay(Inventory inventory,PaymentRequest paymentRequest);

    boolean checkInventoryMoney(PaymentRequest paymentRequest, Inventory inventory);

    PaymentType getType();

    PaymentRequest generatePaymentRequest(OrderRequest request, Product product);

}
