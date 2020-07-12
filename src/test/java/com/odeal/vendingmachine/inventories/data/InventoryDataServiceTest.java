package com.odeal.vendingmachine.inventories.data;

import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.products.data.ProductEntity;
import com.odeal.vendingmachine.products.dtos.Product;
import com.odeal.vendingmachine.products.enums.ProductType;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class InventoryDataServiceTest {

    private InventoryRepository inventoryRepository = Mockito.mock(InventoryRepository.class);
    private InventoryDataService inventoryDataService;

    @BeforeEach
    void setUp() {
        inventoryDataService = new InventoryDataService(inventoryRepository, new ModelMapper());
    }

    @Test
    void saveInventory_success() {
        List<ProductEntity> productEntities = generateProductEntities();
        InventoryEntity inventoryEntity = generateInventoryEntity("inv1", productEntities);
        when(inventoryRepository.save(inventoryEntity)).thenReturn(generateInventoryEntity(1, "inv1", productEntities));
        List<Product> products = generateProducts();
        Inventory inventory = generateInventory("inv1",products);
        Inventory savedInventory = inventoryDataService.save(inventory);
        Inventory expectedInventory = generateInventory(1,"inv1",products);
        assertEquals(expectedInventory,savedInventory);
    }



    private List<ProductEntity> generateProductEntities() {
        return new ArrayList<ProductEntity>() {{
            add(ProductEntity.builder().id(1).name("p1").price(1.12).type(ProductType.FOOD).build());
            add(ProductEntity.builder().id(2).name("p2").price(2.12).type(ProductType.BEVERAGE_HOT).build());
        }};
    }


    private InventoryEntity generateInventoryEntity(String name, List<ProductEntity> productEntities) {
        return InventoryEntity.builder().name(name).products(productEntities).build();
    }

    private InventoryEntity generateInventoryEntity(int id, String name, List<ProductEntity> productEntities) {
        return InventoryEntity.builder().id(id).name(name).products(productEntities).build();
    }

    private Inventory generateInventory(String name, List<Product> products) {
        return Inventory.builder().name(name).products(products).build();
    }

    public static Inventory generateInventory(int id, String name, List<Product> products) {
        return Inventory.builder().id(id).name(name).products(products).build();
    }

    public static List<Product> generateProducts() {
        return new ArrayList<Product>() {{
            add(Product.builder().id(1).name("p1").price(1.12).type(ProductType.FOOD).build());
            add(Product.builder().id(2).name("p2").price(2.12).type(ProductType.BEVERAGE_HOT).build());
        }};
    }

}
