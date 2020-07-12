package com.odeal.vendingmachine.payments.services;

import com.odeal.vendingmachine.inventories.data.InventoryDataService;
import com.odeal.vendingmachine.payments.data.MoneyDataService;
import com.odeal.vendingmachine.payments.dtos.Money;
import com.odeal.vendingmachine.payments.dtos.MoneyType;
import com.odeal.vendingmachine.payments.exceptions.PaymentServiceMoneyNotEnoughException;
import com.odeal.vendingmachine.payments.exceptions.PaymentServiceNotEnoughChangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CashPaymentServiceTest {

    private final InventoryDataService inventoryDataService = Mockito.mock(InventoryDataService.class);
    private CashPaymentService cashPaymentService;

    @BeforeEach
    void setUp() {
        cashPaymentService = new CashPaymentService(inventoryDataService);
    }


    @Test
    void test_calculateChangeMoneyCounts_withDifferentParameters_shouldPass() {
        for (double v=0.05 ; v <= 200 ; v+=0.05 ){
            //when(moneyDataService.findAll()).thenReturn(generateMoneys(2));
            List<Money> moneyList = generateMoneys(2);
            List<Money> map;
            try {
                map = cashPaymentService.calculateChangeMoneyCounts(v,moneyList);
            }catch (PaymentServiceNotEnoughChangeException e){
                continue;
            }
            BigDecimal total = map.stream()
                    .map(x -> BigDecimal.valueOf(x.getValue()).multiply(BigDecimal.valueOf(x.getAmount())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2,BigDecimal.ROUND_HALF_UP);
            assertEquals(BigDecimal.valueOf(v).setScale(2,RoundingMode.HALF_UP),total);
        }
    }


    private List<Money> generateMoneys(int amount){
        return new ArrayList<Money>(){{
            add(Money.builder().value(0.05).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(0.10).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(0.25).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(0.50).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(1.00).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(5.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(10.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(20.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(50.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(100.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(200.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
        }};
    }
}
