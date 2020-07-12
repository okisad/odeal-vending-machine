package com.odeal.vendingmachine.payments.services;


import com.odeal.vendingmachine.inventories.data.InventoryDataService;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.orders.controllers.requests.OrderRequest;
import com.odeal.vendingmachine.payments.dtos.*;
import com.odeal.vendingmachine.payments.exceptions.PaymentServiceMoneyNotEnoughException;
import com.odeal.vendingmachine.payments.exceptions.PaymentServiceNotEnoughChangeException;
import com.odeal.vendingmachine.products.dtos.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CashPaymentService implements PaymentService {

    private final PaymentType paymentType = PaymentType.CASH;
    private final InventoryDataService inventoryDataService;

    @Override
    public PaymentResult pay(Inventory inventory,PaymentRequest paymentRequest) {
        Double totalPrice = paymentRequest.getProduct().getPrice() * paymentRequest.getAmount();
        List<Money> changeMoneys = calculateChange(inventory,paymentRequest, totalPrice);
        updateMoneyCounts(inventory,changeMoneys,paymentRequest);
        BigDecimal change = changeMoneys.stream()
                .map(x -> BigDecimal.valueOf(x.getValue()).multiply(BigDecimal.valueOf(x.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2,BigDecimal.ROUND_HALF_UP);
        return PaymentResult.builder()
                .paymentType(paymentType)
                .paymentAmount(paymentRequest.getLoadedMoney().doubleValue())
                .paymentStatus(PaymentStatus.SUCCESS)
                .change(change.doubleValue())
                .changeMoneys(changeMoneys)
                .paymentAmount(totalPrice)
                .build();
    }

    private void updateMoneyCounts(Inventory inventory, List<Money> withdrawnMoneyList,PaymentRequest paymentRequest){
        inventory.getMoneyList().forEach(m -> {
            paymentRequest.getLoadedMoneyList().stream()
                    .filter(wm -> wm.getValue().equals(m.getValue()))
                    .findAny()
                    .ifPresent(founded -> m.setAmount(m.getAmount() + founded.getAmount()));
        });
        inventory.getMoneyList().forEach(m -> {
            withdrawnMoneyList.stream()
                    .filter(wm -> wm.getValue().equals(m.getValue()))
                    .findAny()
                    .ifPresent(founded -> m.setAmount(m.getAmount() - founded.getAmount()));
        });
        inventoryDataService.save(inventory);
    }

    @Override
    public PaymentType getType() {
        return paymentType;
    }

    @Override
    public PaymentRequest generatePaymentRequest(OrderRequest request, Product product){
        return PaymentRequest.builder()
                .loadedMoney(request.getLoadedMoneyList().stream().map(m -> BigDecimal.valueOf(m.getValue() * m.getAmount()).setScale(2,BigDecimal.ROUND_HALF_UP)).reduce(BigDecimal.ZERO, BigDecimal::add))
                .loadedMoneyList(request.getLoadedMoneyList())
                .product(product)
                .amount(request.getProductAmount()).build();
    }

    @Override
    public boolean checkInventoryMoney(PaymentRequest paymentRequest, Inventory inventory){
        BigDecimal totalPrice = BigDecimal.valueOf(paymentRequest.getProduct().getPrice() * paymentRequest.getAmount()).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal loadedMoney = paymentRequest.getLoadedMoney().setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal change = loadedMoney.subtract(totalPrice).setScale(2,BigDecimal.ROUND_HALF_UP);
        if (change.doubleValue() < 0) throw new PaymentServiceMoneyNotEnoughException(paymentRequest.getLoadedMoneyList());
        List<Money> incomingMoney = paymentRequest.getLoadedMoneyList();
        List<Money> existMoney = inventory.getMoneyList();
        List<Money> tempMoney = addUpToMoney(incomingMoney,existMoney)
                .stream()
                .sorted(Comparator.comparingDouble(Money::getValue).reversed())
                .collect(Collectors.toList());
        return checkEnoughChangeMoney(tempMoney,change.doubleValue());
    }

    private List<Money> addUpToMoney(List<Money> incomingMoney,List<Money> existMoney){
        List<Money> sumMoney = new ArrayList<>();
        for (Money im : existMoney) {
            Optional<Money> optionalIncomingMoney = incomingMoney.stream().filter(m -> m.getValue().equals(im.getValue())).findAny();
            if (optionalIncomingMoney.isPresent()) {
                Money m = optionalIncomingMoney.get();
                sumMoney.add(Money.builder().id(im.getId()).value(m.getValue()).moneyType(m.getMoneyType()).amount(m.getAmount() + im.getAmount()).build());
            } else {
                sumMoney.add(Money.builder().id(im.getId()).value(im.getValue()).moneyType(im.getMoneyType()).amount(im.getAmount()).build());
            }
        }
        return sumMoney;
    }

    private List<Money> calculateChange(Inventory inventory,PaymentRequest request, double totalPrice) {
        if (request.getLoadedMoney().doubleValue() < totalPrice) throw new PaymentServiceMoneyNotEnoughException();
        double change = request.getLoadedMoney().doubleValue() - totalPrice;
        List<Money> moneyList = inventory.getMoneyList().stream()
                //.filter(m -> m.getAmount() > 0)
                .sorted(Comparator.comparingDouble(Money::getValue).reversed())
                .collect(Collectors.toList());
        List<Money> tempMoney = addUpToMoney(request.getLoadedMoneyList(),moneyList);
        return calculateChangeMoneyCounts(change, tempMoney);
    }

    private boolean checkEnoughChangeMoney(List<Money> moneyList,double change){
        Map<Double, Integer> map = new HashMap<>();
        rec(moneyList, BigDecimal.valueOf(change), map);
        BigDecimal total = map.entrySet().stream()
                .map(x -> BigDecimal.valueOf(x.getValue()).multiply(BigDecimal.valueOf(x.getKey())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.valueOf(change).setScale(2, RoundingMode.HALF_UP)) == 0;
    }

    public List<Money> calculateChangeMoneyCounts(double change,List<Money> moneyList) {
        Map<Double, Integer> map = new HashMap<>();
        rec(moneyList, BigDecimal.valueOf(change), map);
        BigDecimal total = map.entrySet().stream()
                .map(x -> BigDecimal.valueOf(x.getValue()).multiply(BigDecimal.valueOf(x.getKey())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.valueOf(change).setScale(2, RoundingMode.HALF_UP)) != 0)
            throw new PaymentServiceNotEnoughChangeException();
        List<Money> result = new ArrayList<>();
        map.forEach((key, value) -> {
            Money money = moneyList.stream().filter(m -> m.getValue().equals(key)).findAny().orElse(null);
            assert money != null;
            result.add(Money.builder().amount(value).moneyType(money.getMoneyType()).value(money.getValue()).build());
        });
        return result;
    }

    public void rec(List<Money> moneyList, BigDecimal change, Map<Double, Integer> map) {
        for (Money money : moneyList) {
            if (change.setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.valueOf(money.getValue()).setScale(2, RoundingMode.HALF_UP)) < 0 || money.getAmount() < 1) continue;
            putToMap(map, money);
            change = change.subtract(BigDecimal.valueOf(money.getValue()));
            if (change.compareTo(BigDecimal.ZERO) > 0)
                rec(moneyList, change, map);
            break;
        }
    }

    private void putToMap(Map<Double, Integer> map, Money money) {
        if (!map.containsKey(money.getValue()))
            map.put(money.getValue(), 1);
        else
            map.put(money.getValue(), map.get(money.getValue()) + 1);
        money.setAmount(money.getAmount() - 1);
    }


    public CashPaymentService(InventoryDataService inventoryDataService) {
        this.inventoryDataService = inventoryDataService;
    }
}
