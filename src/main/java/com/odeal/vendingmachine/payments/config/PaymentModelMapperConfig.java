package com.odeal.vendingmachine.payments.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentModelMapperConfig {


    @Bean
    public ModelMapper paymentModelMapper(){
        return new ModelMapper();
    }

}
