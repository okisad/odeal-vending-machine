package com.odeal.vendingmachine.inventories.services;

import com.odeal.vendingmachine.inventories.data.InventoryDataService;
import com.odeal.vendingmachine.inventories.data.InventoryDataServiceTest;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.inventories.exceptions.InventoryNotFoundException;
import com.odeal.vendingmachine.inventories.exceptions.InventoryProductNotSufficientException;
import com.odeal.vendingmachine.products.data.ProductDataService;
import com.odeal.vendingmachine.products.dtos.Product;
import com.odeal.vendingmachine.products.utils.RandomProductGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/*@SpringBootTest*/
class InventoryServiceTest1 {

    /*private static final int COUNT_OF_ITEMS = 3;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductDataService productDataService;
    @Autowired
    private InventoryDataService inventoryDataService;

    @BeforeEach
    void setUp() {
        List<Product> productList = RandomProductGenerator.generate(5, 5, 20);
        productList = productDataService.saveAll(productList);
        List<Product> products = new ArrayList<>();
        productList.forEach(p -> products.addAll(duplicate(p)));
        Inventory inventory = Inventory.builder()
                .name("Inventory 1")
                .products(products)
                .build();
        inventoryDataService.save(inventory);
    }

    *//*@Test
    void test_removeProduct_withAmountOfTwoOfProduct_shouldRemoveTwoProduct(){
        inventoryService.removeProduct(1,1,2);
        Inventory inventory = inventoryDataService.findById(1).orElseThrow(InventoryNotFoundException::new);
        assertEquals(inventory.getProducts().size(), 88);
        assertEquals(inventory.getProducts().stream().filter(p -> p.getId() == 1).count(), 1);
    }

    @Test
    void test_removeProduct_withAmountOfThreeOfProduct_shouldRemoveThreeProduct(){
        inventoryService.removeProduct(1,2,3);
        Inventory inventory = inventoryDataService.findById(1).orElseThrow(InventoryNotFoundException::new);
        assertEquals(inventory.getProducts().size(), 85);
        assertEquals(inventory.getProducts().stream().filter(p -> p.getId() == 2).count(), 0);
    }*//*

    @Test
    void test_removeProduct_withNotExistProductId_shouldThrowInventoryNotFoundException(){
        try {
            inventoryService.removeProduct(10,2,3);
            fail();
        }catch (InventoryNotFoundException e){
            assertEquals("inventory is not found", e.getMessage());
        }
    }

    @Test
    void test_removeProduct_withNoSufficientProductAmount_shouldThrowInventoryProductNotSufficientException(){
        try {
            inventoryService.removeProduct(2,99,3);
            fail();
        }catch (InventoryProductNotSufficientException e){
            assertEquals("Product count is not sufficent", e.getMessage());
        }
    }

    *//*@Test
    void test_findInventory_withById_shouldGetOptionalInventory(){
        List<Product> products = InventoryDataServiceTest.generateProducts();
        Inventory inventory = InventoryDataServiceTest.generateInventory(1,"inv1",products);
        when(inventoryDataService.findById(1)).thenReturn(Optional.ofNullable(inventory));
        Optional<Inventory> optionalInventory = inventoryService.findById(1);
        assertNotNull(optionalInventory);
        assertTrue(optionalInventory.isPresent());
        assertEquals(inventory,optionalInventory.get());
    }*//*


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
    }*/
}