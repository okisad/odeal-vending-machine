package com.odeal.vendingmachine.payments.data;

import com.odeal.vendingmachine.inventories.data.InventoryDataService;
import com.odeal.vendingmachine.inventories.data.InventoryEntity;
import com.odeal.vendingmachine.inventories.data.InventoryRepository;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.payments.dtos.Money;
import com.odeal.vendingmachine.payments.dtos.MoneyType;
import com.odeal.vendingmachine.products.data.ProductEntity;
import com.odeal.vendingmachine.products.dtos.Product;
import com.odeal.vendingmachine.products.enums.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MoneyDataServiceTest {

    private MoneyRepository moneyRepository = Mockito.mock(MoneyRepository.class);
    private MoneyDataService moneyDataService;

    @BeforeEach
    void setUp() {
        moneyDataService = new MoneyDataService(new ModelMapper(), moneyRepository);
    }

    @Test
    void test_saveAllMoney_withMoneys_shouldSaved() {
        List<Money> moneyList = generateMoneyListWithoutId();
        List<Money> expectedMoneyList = generateMoneyListWithId();
        List<MoneyEntity> moneyEntitiesOutput = generateMoneyEntityListWithId();
        List<MoneyEntity> moneyEntitiesInput = generateMoneyEntityListWithoutId();
        when(moneyRepository.saveAll(moneyEntitiesInput)).thenReturn(moneyEntitiesOutput);
        List<Money> actualMoneyList = moneyDataService.saveAll(moneyList);
        assertEquals(expectedMoneyList, actualMoneyList);
    }

    @Test
    void test_findAllMoney_withZeroArguments_shouldFind() {
        List<Money> expectedMoneyList = generateMoneyListWithId();
        List<MoneyEntity> moneyEntitiesOutput = generateMoneyEntityListWithId();
        when(moneyRepository.findAll()).thenReturn(moneyEntitiesOutput);
        List<Money> actualMoneyList = moneyDataService.findAll();
        assertEquals(expectedMoneyList, actualMoneyList);
    }


    public static List<MoneyEntity> generateMoneyEntityListWithoutId() {
        return new ArrayList<MoneyEntity>() {{
            add(MoneyEntity.builder().moneyType(MoneyType.COIN).value(1.00).amount(3).build());
            add(MoneyEntity.builder().moneyType(MoneyType.BANK_NOTE).value(5.00).amount(3).build());
        }};
    }

    public static List<MoneyEntity> generateMoneyEntityListWithId() {
        return new ArrayList<MoneyEntity>() {{
            add(MoneyEntity.builder().id(1).moneyType(MoneyType.COIN).value(1.00).amount(3).build());
            add(MoneyEntity.builder().id(2).moneyType(MoneyType.BANK_NOTE).value(5.00).amount(3).build());
        }};
    }

    public static List<Money> generateMoneyListWithId() {
        return new ArrayList<Money>() {{
            add(Money.builder().id(1).moneyType(MoneyType.COIN).value(1.00).amount(3).build());
            add(Money.builder().id(2).moneyType(MoneyType.BANK_NOTE).value(5.00).amount(3).build());
        }};
    }

    public static List<Money> generateMoneyListWithoutId() {
        return new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.COIN).value(1.00).amount(3).build());
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).value(5.00).amount(3).build());
        }};
    }
}