package com.odeal.vendingmachine;

import com.odeal.vendingmachine.inventories.data.InventoryDataService;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.payments.data.MoneyDataService;
import com.odeal.vendingmachine.payments.dtos.Money;
import com.odeal.vendingmachine.payments.dtos.MoneyType;
import com.odeal.vendingmachine.products.data.ProductDataService;
import com.odeal.vendingmachine.products.dtos.Product;
import com.odeal.vendingmachine.products.utils.RandomProductGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class VendingMachineApplicationTests {

    @Autowired
    private InventoryDataService inventoryDataService;
    @Autowired
    private ProductDataService productDataService;
    @Autowired
    private MoneyDataService moneyDataService;

    private static final int COUNT_OF_ITEMS = 3;

    @Test
    void contextLoads() {
        List<Product> productList = RandomProductGenerator.generate(5, 5, 20);
        productList = productDataService.saveAll(productList);
        List<Product> products = new ArrayList<>();
        productList.forEach(p -> products.addAll(duplicate(p)));
        Inventory inventory = Inventory.builder()
                .name("Inventory 1")
                .products(products)
                .sugarCount(500)
                .build();
        inventoryDataService.save(inventory);
        moneyDataService.saveAll(generateMoneys(1));
    }

    public List<Product> duplicate(Product p) {
        List<Product> products = new ArrayList<>();
        IntStream.range(0, COUNT_OF_ITEMS).forEach(i -> {
            products.add(Product.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .type(p.getType())
                    .price(p.getPrice()).build()
            );
        });
        return products;
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
