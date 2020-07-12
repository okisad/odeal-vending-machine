package com.odeal.vendingmachine.products.utils;

import com.odeal.vendingmachine.products.dtos.Product;
import com.odeal.vendingmachine.products.enums.ProductType;
import com.odeal.vendingmachine.products.utils.exceptions.WrongArgumentException;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


import static org.junit.jupiter.api.Assertions.*;

class RandomProductGeneratorTest {

    @Test
    void generate() {
        int beverageColdSize = 5;
        int beverageHotSize = 5;
        int foodSize = 20;
        List<Product> products = RandomProductGenerator.generate(beverageColdSize,beverageHotSize,foodSize);
        assertFalse(CollectionUtils.isEmpty(products));
        assertEquals(products.size(),beverageColdSize+beverageHotSize+foodSize);
        assertEquals(products.stream().filter(p -> p.getType() == ProductType.FOOD).count(),foodSize);
        assertEquals(products.stream().filter(p -> p.getType() == ProductType.BEVERAGE_HOT).count(),beverageHotSize);
        assertEquals(products.stream().filter(p -> p.getType() == ProductType.BEVERAGE_COLD).count(),beverageColdSize);
        products.forEach(p -> {
            assertFalse(StringUtils.isEmpty(p.getName()));
            assertNotNull(p.getPrice());
            assertTrue(p.getPrice() >= 1);
            assertTrue(p.getPrice() <= 5);
        });
    }


    @Test
    void generateRandomNumbers(){
        List<Double> dividers = new ArrayList<Double>(){{
            add(0.05);add(0.1);add(0.15);add(0.2);add(0.25);
        }};
        IntStream.range(0,1000).forEach(i -> {
            int start = i;
            int end = i + ((new Random()).nextInt(10) +1);
            double divider = dividers.get(new Random().nextInt(dividers.size()));
            List<Double> values = null;
            try {
                values = RandomProductGenerator.findDoubleListBoundedWithDivider(start,end,divider);
            } catch (WrongArgumentException e) {
                fail(e.getMessage());
            }
            Assert.isTrue(!CollectionUtils.isEmpty(values));
            values.forEach(v -> {
                Assert.isTrue(Math.abs(v % divider) < divider);
                Assert.isTrue(v >= start);
                Assert.isTrue(v <= end);
            });
        });
    }


    @Test
    void testWrongExceptionGenerateRandomNumbers(){
        try {
            List<Double> values = RandomProductGenerator.findDoubleListBoundedWithDivider(3,1,0.05);
            fail("Exception not thrown");
        } catch (WrongArgumentException e) {
            assertEquals("Start value should be lower than end value or Divider sould not be 0", e.getMessage());
        }

        try {
            List<Double> values = RandomProductGenerator.findDoubleListBoundedWithDivider(5,10,0);
            fail("Exception not thrown");
        } catch (WrongArgumentException e) {
            assertEquals("Start value should be lower than end value or Divider sould not be 0", e.getMessage());
        }
    }

}