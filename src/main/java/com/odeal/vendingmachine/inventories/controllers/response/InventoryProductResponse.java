package com.odeal.vendingmachine.inventories.controllers.response;


import com.odeal.vendingmachine.products.enums.ProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventoryProductResponse {

    private Integer id;
    private String name;
    private Integer amount;
    private Double price;
    private ProductType productType;

}
