package com.odeal.vendingmachine.products.services;

import com.odeal.vendingmachine.products.data.ProductDataService;
import com.odeal.vendingmachine.products.dtos.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductDataService productDataService;

    public Optional<Product> findById(int id){
        return productDataService.findById(id);
    }

    public ProductService(ProductDataService productDataService) {
        this.productDataService = productDataService;
    }
}
