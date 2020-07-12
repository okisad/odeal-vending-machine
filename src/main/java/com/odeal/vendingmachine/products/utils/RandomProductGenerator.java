package com.odeal.vendingmachine.products.utils;

import com.odeal.vendingmachine.products.dtos.Product;
import com.odeal.vendingmachine.products.enums.ProductType;
import com.odeal.vendingmachine.products.utils.exceptions.WrongArgumentException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomProductGenerator {

    private static final int START = 1;
    private static final int END = 5;
    private static final double DIVIDER = 0.05;

    public static List<Product> generate(int beverageColdSize, int beverageHotSize, int foodSize) throws WrongArgumentException {
        List<Product> products = new ArrayList<>();
        products.addAll(generateProductWithTypeAndSize(ProductType.BEVERAGE_COLD, beverageColdSize));
        products.addAll(generateProductWithTypeAndSize(ProductType.BEVERAGE_HOT, beverageHotSize));
        products.addAll(generateProductWithTypeAndSize(ProductType.FOOD, foodSize));
        return products;
    }

    public static List<Product> generateProductWithTypeAndSize(ProductType type, int size) {
        List<Product> products = new ArrayList<>();
        List<Double> doubleValues = findDoubleListBoundedWithDivider(START, END, DIVIDER);
        IntStream.range(0, size).forEach(i ->
                products.add(
                        Product.builder()
                                .name(RandomStringUtils.randomAlphabetic(5))
                                .price(findRandomNumberBoundedWithDivider(doubleValues))
                                .type(type).build()
                )
        );
        return products;
    }

    public static List<Double> findDoubleListBoundedWithDivider(int start, int end, double divider) {
        if (start >= end || divider == 0.0)
            throw new WrongArgumentException("Start value should be lower than end value or Divider sould not be 0");
        List<Integer> integers = new ArrayList<>();
        int startS = start * 100;
        int endS = end * 100;
        int dividerS = (int) (divider * 100);
        for (int i = startS; i <= endS; i++) {
            if (i % dividerS == 0)
                integers.add(i);
        }
        return integers.stream().map(i -> (double) i / 100.0).collect(Collectors.toList());
    }

    public static Double findRandomNumberBoundedWithDivider(List<Double> values) {
        if (CollectionUtils.isEmpty(values))
            throw new WrongArgumentException("List should not be empty");
        return values.get(new Random().nextInt(values.size()));
    }


}
