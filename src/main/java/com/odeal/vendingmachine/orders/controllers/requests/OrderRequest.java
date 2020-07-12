package com.odeal.vendingmachine.orders.controllers.requests;

import com.odeal.vendingmachine.payments.dtos.Money;
import com.odeal.vendingmachine.payments.dtos.PaymentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @ApiModelProperty(example = "1",required = true)
    @NotNull(message = "product id is mandatory")
    @Min(value = 1,message = "product should be minimum 1")
    private Integer productId;
    @NotNull(message = "product amount is mandatory")
    @Min(value = 1,message = "product amount should be minimum 1")
    @ApiModelProperty(example = "2",required = true)
    private Integer productAmount;
    @ApiModelProperty(example = "2",required = false)
    private Integer productSugarAmount;
    @ApiModelProperty(example = "1212",required = false)
    private Integer creditCardPin;
    @NotNull(message = "payment type should be selected")
    @ApiModelProperty(example = "CASH",required = true)
    private PaymentType paymentType;
    @ApiModelProperty(required = false)
    private List<Money> loadedMoneyList;

}

