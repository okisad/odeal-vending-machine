package com.odeal.vendingmachine.payments.dtos;


import com.odeal.vendingmachine.products.dtos.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private Product product;
    private Integer amount;
    private BigDecimal loadedMoney;
    private List<Money> loadedMoneyList;
    private Integer creditCardPin;
}
