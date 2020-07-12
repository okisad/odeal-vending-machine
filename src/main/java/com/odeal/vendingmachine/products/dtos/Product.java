package com.odeal.vendingmachine.products.dtos;

import com.odeal.vendingmachine.products.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private int id;
    private String name;
    private Double price;
    private ProductType type;
}
