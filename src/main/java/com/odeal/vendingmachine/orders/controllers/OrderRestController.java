package com.odeal.vendingmachine.orders.controllers;

import com.odeal.vendingmachine.orders.controllers.requests.OrderRequest;
import com.odeal.vendingmachine.orders.controllers.responses.OrderResponse;
import com.odeal.vendingmachine.orders.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    private final OrderService orderService;


    @PostMapping("inventories/{inventoryId}")
    public ResponseEntity<OrderResponse> order(@PathVariable("inventoryId") int inventoryId, @RequestBody @Valid OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.order(inventoryId,orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }
}
