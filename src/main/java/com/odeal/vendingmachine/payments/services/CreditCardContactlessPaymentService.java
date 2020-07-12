package com.odeal.vendingmachine.payments.services;

import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.orders.controllers.requests.OrderRequest;
import com.odeal.vendingmachine.payments.dtos.PaymentRequest;
import com.odeal.vendingmachine.payments.dtos.PaymentResult;
import com.odeal.vendingmachine.payments.dtos.PaymentStatus;
import com.odeal.vendingmachine.payments.dtos.PaymentType;
import com.odeal.vendingmachine.products.dtos.Product;
import org.springframework.stereotype.Service;

@Service
public class CreditCardContactlessPaymentService extends PaymentServiceAbstract implements PaymentService{

    private final PaymentType paymentType = PaymentType.CREDIT_CARD_CONTACTLESS;

    @Override
    public PaymentResult pay(Inventory inventory, PaymentRequest paymentRequest) {
        Double totalPrice = getTotalPrice(paymentRequest);
        return PaymentResult.builder()
                .paymentAmount(totalPrice)
                .paymentType(paymentType)
                .paymentStatus(PaymentStatus.SUCCESS)
                .build();
    }

    @Override
    public boolean checkInventoryMoney(PaymentRequest paymentRequest, Inventory inventory) {
        return true;
    }


    @Override
    public PaymentType getType() {
        return paymentType;
    }

    @Override
    public PaymentRequest generatePaymentRequest(OrderRequest request, Product product){
        return PaymentRequest.builder()
                //.loadedMoneyList(request.getLoadedMoneyList())
                //.loadedMoney(request.getLoadedMoneyList().stream().map(m -> BigDecimal.valueOf(m.getValue() * m.getAmount()).setScale(2,BigDecimal.ROUND_HALF_UP)).reduce(BigDecimal.ZERO, BigDecimal::add))
                .product(product)
                .amount(request.getProductAmount()).build();
    }
}
