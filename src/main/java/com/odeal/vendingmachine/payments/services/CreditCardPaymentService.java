package com.odeal.vendingmachine.payments.services;

import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.orders.controllers.requests.OrderRequest;
import com.odeal.vendingmachine.payments.dtos.PaymentRequest;
import com.odeal.vendingmachine.payments.dtos.PaymentResult;
import com.odeal.vendingmachine.payments.dtos.PaymentStatus;
import com.odeal.vendingmachine.payments.dtos.PaymentType;
import com.odeal.vendingmachine.payments.exceptions.PaymentServiceException;
import com.odeal.vendingmachine.products.dtos.Product;
import org.springframework.stereotype.Service;

@Service
public class CreditCardPaymentService extends PaymentServiceAbstract implements PaymentService{

    private final PaymentType paymentType = PaymentType.CREDIT_CARD;

    @Override
    public PaymentResult pay(Inventory inventory, PaymentRequest paymentRequest) {
        Double totalPrice = getTotalPrice(paymentRequest);
        if (paymentRequest.getCreditCardPin() == null || (paymentRequest.getCreditCardPin() < 1000 || paymentRequest.getCreditCardPin() > 9999))
            throw new PaymentServiceException(paymentRequest.getLoadedMoneyList(),"Credit card pin should be entered or between 1000 and 9999");
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
                .product(product)
                .amount(request.getProductAmount())
                .creditCardPin(request.getCreditCardPin()).build();
    }
}
