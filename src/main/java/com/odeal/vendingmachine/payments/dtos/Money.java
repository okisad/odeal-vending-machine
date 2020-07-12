package com.odeal.vendingmachine.payments.dtos;


import com.odeal.vendingmachine.inventories.data.InventoryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Money {

    @ApiModelProperty(hidden = true)
    private Integer id;
    @Min(0)
    @Max(200)
    @ApiModelProperty(required = true,example = "10.0")
    private Double value;
    @NotNull(message = "MoneyType should be declared (BANK_NOTE or COIN)")
    @ApiModelProperty(required = true,example = "BANK_NOTE")
    private MoneyType moneyType;
    @ApiModelProperty(required = true,example = "1")
    private int amount;
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    private InventoryEntity inventory;
}
