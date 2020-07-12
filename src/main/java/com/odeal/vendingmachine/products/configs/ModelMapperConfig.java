package com.odeal.vendingmachine.products.configs;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {


    @Bean
    public ModelMapper productsModelMapper(){
        return new ModelMapper();
    }

}
