package com.odeal.vendingmachine.inventories.services;

import com.odeal.vendingmachine.inventories.data.InventoryDataService;
import com.odeal.vendingmachine.inventories.data.InventoryDataServiceTest;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.products.data.ProductDataService;
import com.odeal.vendingmachine.products.dtos.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceTest {

    private InventoryService inventoryService;
    private InventoryDataService inventoryDataService = Mockito.mock(InventoryDataService.class);

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(inventoryDataService);
    }

    @Test
    void test_removeProduct_withInventoryIdAndProductIdAndCount_shouldBeUpdated() {
        int inventoryId = 1;
        List<Product> products = InventoryDataServiceTest.generateProducts();
        Inventory inventory = InventoryDataServiceTest.generateInventory(1,"inv1",products);
        //when(inventoryDataService.findById(inventoryId)).thenReturn(Optional.of(inventory));
        inventoryService.removeProduct(inventory,1,1);
        verify(inventoryDataService).save(inventory);
    }

    @Test
    void test_findById_withId_shouldBeFound() {
        int inventoryId = 1;
        List<Product> products = InventoryDataServiceTest.generateProducts();
        Inventory inventory = InventoryDataServiceTest.generateInventory(1,"inv1",products);
        when(inventoryDataService.findById(inventoryId)).thenReturn(Optional.of(inventory));
        Optional<Inventory> optionalInventory = inventoryService.findById(inventoryId);
        assertTrue(optionalInventory.isPresent());
        assertEquals(inventoryId,optionalInventory.get().getId());
        assertEquals(products.size(),optionalInventory.get().getProducts().size());
    }
}
