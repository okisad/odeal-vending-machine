package com.odeal.vendingmachine.inventories.data;

import com.odeal.vendingmachine.inventories.dtos.Inventory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class InventoryDataService {


    private final InventoryRepository inventoryRepository;
    private final ModelMapper inventoryModelMapper;

    public Inventory save(Inventory inventory){
        InventoryEntity inventoryEntity = inventoryModelMapper.map(inventory,InventoryEntity.class);
        InventoryEntity savedInventoryEntity = inventoryRepository.save(inventoryEntity);
        return inventoryModelMapper.map(savedInventoryEntity,Inventory.class);
    }


    @Transactional
    public Optional<Inventory> findById(int id){
        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(id);
        optionalInventoryEntity.map(i -> i.getMoneyList().size());
        return optionalInventoryEntity.map(i -> inventoryModelMapper.map(i,Inventory.class));
    }



    public InventoryDataService(InventoryRepository inventoryRepository, ModelMapper inventoryModelMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryModelMapper = inventoryModelMapper;
    }
}
