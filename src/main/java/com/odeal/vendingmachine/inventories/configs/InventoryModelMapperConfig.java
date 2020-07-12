package com.odeal.vendingmachine.inventories.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryModelMapperConfig {

    @Bean
    public ModelMapper inventoryModelMapper(){
        return new ModelMapper();
    }

}
