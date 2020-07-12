package com.odeal.vendingmachine.inventories.dtos;

import com.odeal.vendingmachine.payments.dtos.Money;
import com.odeal.vendingmachine.products.dtos.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    private int id;
    private String name;
    private List<Product> products;
    private Integer sugarCount;
    private List<Money> moneyList;

}
