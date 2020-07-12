package com.odeal.vendingmachine.orders.services;

import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.inventories.exceptions.InventoryNotFoundException;
import com.odeal.vendingmachine.inventories.exceptions.InventoryProductNotSufficientException;
import com.odeal.vendingmachine.inventories.exceptions.InventorySugarNotSufficientException;
import com.odeal.vendingmachine.inventories.services.InventoryService;
import com.odeal.vendingmachine.orders.controllers.requests.OrderRequest;
import com.odeal.vendingmachine.orders.controllers.responses.OrderResponse;
import com.odeal.vendingmachine.payments.dtos.PaymentRequest;
import com.odeal.vendingmachine.payments.dtos.PaymentResult;
import com.odeal.vendingmachine.payments.exceptions.PaymentServiceNotEnoughChangeException;
import com.odeal.vendingmachine.payments.services.PaymentService;
import com.odeal.vendingmachine.payments.services.PaymentServiceFactory;
import com.odeal.vendingmachine.products.dtos.Product;
import com.odeal.vendingmachine.products.enums.ProductType;
import com.odeal.vendingmachine.products.exceptions.ProductNotFoundException;
import com.odeal.vendingmachine.products.services.ProductService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderService {

    private final InventoryService inventoryService;
    private final ProductService productService;
    private final PaymentServiceFactory paymentServiceFactory;

    //para validasyon
    //product yeterlilik kontrolu
    //sicak mi soguk mu kontrolu
    //soguk ve seker ise hata
    //varsa seker kontrolu
    //paranin yeterlilik kontrollu (hem fiyat yeterliligi hemde yeterli para ustu
    //kredi karti ise pin kontrolu
    //odeme isleminin yapilmasi
    //yemek azlatmasi
    //seker guncellemesi
    //hazne guncellemesi

    @Transactional
    public OrderResponse order(Integer inventoryId, OrderRequest orderRequest) {
        Product product = productService.findById(orderRequest.getProductId()).orElseThrow(ProductNotFoundException::new);
        Inventory inventory = inventoryService.findById(inventoryId).orElseThrow(InventoryNotFoundException::new);
        checkProductSufficient(inventory, orderRequest);
        boolean isNeededSugarProcess = checkSugar(product, inventory, orderRequest);
        PaymentResult paymentResult = pay(inventory,orderRequest, product);
        withdrawProcesses(inventory,product,orderRequest,isNeededSugarProcess);
        return buildOrderResponse(product,orderRequest,paymentResult);
    }

    private OrderResponse buildOrderResponse(Product product,OrderRequest orderRequest,PaymentResult paymentResult){
        return OrderResponse.builder()
                .change(paymentResult.getChange())
                .paymentType(orderRequest.getPaymentType())
                .productAmount(orderRequest.getProductAmount())
                .productName(product.getName())
                .changes(paymentResult.getChangeMoneys())
                .totalPrice(paymentResult.getPaymentAmount())
                .build();
    }

    private void withdrawProcesses(Inventory inventory,Product product,OrderRequest orderRequest,boolean isNeededSugarProcess){
        inventoryService.removeProduct(inventory, product.getId(), orderRequest.getProductAmount());
        if (isNeededSugarProcess)
            decreaseSugar(inventory, orderRequest);
    }

    private boolean checkSugar(Product product, Inventory inventory, OrderRequest orderRequest) {
        if (product.getType() != ProductType.BEVERAGE_HOT)
            return false;
        if (orderRequest.getProductSugarAmount() == null)
            return false;
        if (orderRequest.getProductSugarAmount() < 0)
            throw new InventorySugarNotSufficientException("product sugar amount should be minimum 0",orderRequest.getLoadedMoneyList());
        if (orderRequest.getProductSugarAmount() > 5)
            throw new InventorySugarNotSufficientException("product sugar amount should be maximum 5",orderRequest.getLoadedMoneyList());
        if (!inventoryService.checkSugar(inventory, orderRequest.getProductSugarAmount()))
            throw new InventorySugarNotSufficientException(orderRequest.getLoadedMoneyList());

        return true;
    }

    private void decreaseSugar(Inventory inventory, OrderRequest orderRequest) {
        inventoryService.removeSugar(inventory, orderRequest.getProductSugarAmount());
    }

    private void checkProductSufficient(Inventory inventory, OrderRequest orderRequest) {
        if (!inventoryService.checkProductCount(inventory, orderRequest.getProductId(), orderRequest.getProductAmount()))
            throw new InventoryProductNotSufficientException(orderRequest.getLoadedMoneyList());
    }

    private PaymentResult pay(Inventory inventory,OrderRequest orderRequest, Product product) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(orderRequest.getPaymentType());
        PaymentRequest paymentRequest = paymentService.generatePaymentRequest(orderRequest, product);
        if (!paymentService.checkInventoryMoney(paymentRequest,inventory)) throw new PaymentServiceNotEnoughChangeException(orderRequest.getLoadedMoneyList());
        return paymentService.pay(inventory,paymentRequest);
    }

    public OrderService(InventoryService inventoryService, ProductService productService, PaymentServiceFactory paymentServiceFactory) {
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.paymentServiceFactory = paymentServiceFactory;
    }
}
