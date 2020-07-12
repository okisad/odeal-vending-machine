package com.odeal.vendingmachine.payments.data;

import com.odeal.vendingmachine.inventories.data.InventoryEntity;
import com.odeal.vendingmachine.payments.dtos.MoneyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "moneys")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double value;
    @Enumerated(EnumType.STRING)
    private MoneyType moneyType;
    private int amount;
    @ManyToOne
    @JoinColumn(name="inventory_id")
    private InventoryEntity inventory;

}
