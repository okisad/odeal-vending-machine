package com.odeal.vendingmachine.inventories.controllers;


import com.odeal.vendingmachine.inventories.controllers.response.InventoryResponse;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.inventories.services.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/inventories")
public class InventoryRestController {

    private final InventoryService inventoryService;


    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponse> findInventoryById(@PathVariable("inventoryId") int inventoryId){
        Optional<Inventory> inventory = inventoryService.findById(inventoryId);
        return inventory
                .map(value -> ResponseEntity.ok(InventoryResponse.of(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    public InventoryRestController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
}
