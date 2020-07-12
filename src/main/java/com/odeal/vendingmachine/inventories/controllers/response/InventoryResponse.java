package com.odeal.vendingmachine.inventories.controllers.response;


import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.products.dtos.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter@Setter
@Builder
public class InventoryResponse {

    private Integer id;
    private Integer totalProductAmount;
    private List<InventoryProductResponse> products;
    private Integer sugarCount;

    public static InventoryResponse of(Inventory inventory){
        Map<Product,Long> productCountMap = inventory.getProducts()
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<InventoryProductResponse> inventoryProductResponses = new ArrayList<>();
        AtomicInteger totalProductCount = new AtomicInteger(0);
        productCountMap.forEach((k,v) -> {
            totalProductCount.addAndGet(v.intValue());
            inventoryProductResponses.add(InventoryProductResponse.builder().name(k.getName()).amount(v.intValue()).productType(k.getType()).id(k.getId()).price(k.getPrice()).build());
        });
        return InventoryResponse.builder().id(inventory.getId()).totalProductAmount(totalProductCount.get()).products(inventoryProductResponses).sugarCount(inventory.getSugarCount()).build();
    }
}
