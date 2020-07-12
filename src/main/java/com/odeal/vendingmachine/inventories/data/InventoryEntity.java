package com.odeal.vendingmachine.inventories.data;

import com.odeal.vendingmachine.payments.data.MoneyEntity;
import com.odeal.vendingmachine.products.data.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "inventories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="inventory_product_table",
            joinColumns={@JoinColumn(name="inventory_id")},
            inverseJoinColumns={@JoinColumn(name="product_id")})
    private List<ProductEntity> products;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="inventory_id",referencedColumnName="id")
    private List<MoneyEntity> moneyList;
    private Integer sugarCount;

}
