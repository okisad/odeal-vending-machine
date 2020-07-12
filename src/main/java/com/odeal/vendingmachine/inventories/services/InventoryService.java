package com.odeal.vendingmachine.inventories.services;

import com.odeal.vendingmachine.inventories.data.InventoryDataService;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.inventories.exceptions.InventoryProductNotSufficientException;
import com.odeal.vendingmachine.products.dtos.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryDataService inventoryDataService;

    public void removeProduct(Inventory inventory,int productId,int productAmount){
        List<Product> existProducts = inventory.getProducts().stream().filter(p -> p.getId() == productId).collect(Collectors.toList());
        if (!checkProductCount(inventory,productId,productAmount))throw new InventoryProductNotSufficientException();
        inventory.setProducts(inventory.getProducts().stream().filter(p -> p.getId() != productId).collect(Collectors.toList()));
        inventory.getProducts().addAll(existProducts.stream().limit(existProducts.size()-productAmount).collect(Collectors.toList()));
        inventoryDataService.save(inventory);
    }

    public boolean checkProductCount(Inventory inventory, int productId, int productAmount){
        int totalProduct = (int) inventory.getProducts().stream().filter(p -> p.getId() == productId).count();
        return totalProduct >= productAmount;
    }


    public boolean checkSugar(Inventory inventory,int amountOfSugar){
        return inventory.getSugarCount() >= amountOfSugar;
    }

    public void removeSugar(Inventory inventory,int amountOfSugar){
        inventory.setSugarCount(inventory.getSugarCount() - amountOfSugar);
        inventoryDataService.save(inventory);
    }

    public Optional<Inventory> findById(int inventoryId){
        return inventoryDataService.findById(inventoryId);
    }


    public InventoryService(InventoryDataService inventoryDataService) {
        this.inventoryDataService = inventoryDataService;
    }
}
