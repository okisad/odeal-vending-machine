package com.odeal.vendingmachine.payments.data;

import com.odeal.vendingmachine.payments.dtos.Money;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoneyDataService {

    private final ModelMapper paymentModelMapper;
    private final MoneyRepository moneyRepository;


    public List<Money> saveAll(List<Money> moneyList) {
        List<MoneyEntity> moneyEntities = moneyList.stream().map(m -> paymentModelMapper.map(m, MoneyEntity.class)).collect(Collectors.toList());
        moneyEntities = moneyRepository.saveAll(moneyEntities);
        return moneyEntities.stream().map(m -> paymentModelMapper.map(m,Money.class)).collect(Collectors.toList());
    }

    public List<Money> findAll(){
        List<MoneyEntity> moneyEntities = moneyRepository.findAll();
        return moneyEntities.stream().map(m -> paymentModelMapper.map(m,Money.class)).collect(Collectors.toList());
    }

    public MoneyDataService(ModelMapper paymentModelMapper, MoneyRepository moneyRepository) {
        this.paymentModelMapper = paymentModelMapper;
        this.moneyRepository = moneyRepository;
    }
}
