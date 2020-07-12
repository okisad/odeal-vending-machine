package com.odeal.vendingmachine.orders;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.odeal.vendingmachine.inventories.controllers.response.InventoryProductResponse;
import com.odeal.vendingmachine.inventories.controllers.response.InventoryResponse;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.inventories.services.InventoryService;
import com.odeal.vendingmachine.orders.controllers.requests.OrderRequest;
import com.odeal.vendingmachine.orders.controllers.responses.OrderResponse;
import com.odeal.vendingmachine.payments.dtos.Money;
import com.odeal.vendingmachine.payments.dtos.MoneyType;
import com.odeal.vendingmachine.payments.dtos.PaymentType;
import com.odeal.vendingmachine.products.enums.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class OrderIntegrationTest {


    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private InventoryService inventoryService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    void test_order_withWrongId_returnError() throws Exception {
        OrderRequest orderRequest = generateOrderRequest(-1,2,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(2).value(5.0).build());
        }});

        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);

        MvcResult mvcResult = mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace("[\"product should be minimum 1\"]");
    }

    @Test
    void test_order_withLessOneProductAmountAndMoreSufficientAmount_returnError() throws Exception {
        OrderRequest orderRequest = generateOrderRequest(1,-1,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(2).value(5.0).build());
        }});

        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);

        MvcResult mvcResult = mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace("[\"product amount should be minimum 1\"]");
        orderRequest.setProductAmount(10000);
        json = gson.toJson(orderRequest);
        mvcResult = mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Product count is not sufficent"))
                .andReturn();

    }

    @Test
    void test_order_withHotBeverageAndWrongSugarCount_returnError() throws Exception {
        Gson gson = new Gson();
        InventoryResponse inventoryResponse = findInventory(1);
        assertNotNull(inventoryResponse);
        assertNotNull(inventoryResponse.getProducts());
        assertTrue(inventoryResponse.getProducts().size() > 0);
        Optional<InventoryProductResponse> optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getProductType() == ProductType.BEVERAGE_HOT).findAny();
        assertTrue(optionalProduct.isPresent());
        OrderRequest orderRequest = generateOrderRequest(optionalProduct.get().getId(),1,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(2).value(5.0).build());
        }},10000);

        String json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("product sugar amount should be maximum 5"))
                .andReturn();


        orderRequest = generateOrderRequest(optionalProduct.get().getId(),1,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(2).value(5.0).build());
        }},6);

        json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("product sugar amount should be maximum 5"))
                .andReturn();


        orderRequest = generateOrderRequest(optionalProduct.get().getId(),1,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(2).value(5.0).build());
        }},-1);

        json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("product sugar amount should be minimum 0"))
                .andReturn();

    }

    @Test
    void test_order_withNonHotBeverageAndSugar_returnIgnoreSugarSuccess() throws Exception {
        Gson gson = new Gson();
        InventoryResponse inventoryResponse = findInventory(1);
        assertNotNull(inventoryResponse);
        assertNotNull(inventoryResponse.getProducts());
        assertTrue(inventoryResponse.getProducts().size() > 0);
        Optional<InventoryProductResponse> optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getProductType() != ProductType.BEVERAGE_HOT).findAny();
        assertTrue(optionalProduct.isPresent());
        OrderRequest orderRequest = generateOrderRequest(optionalProduct.get().getId(),1,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(2).value(5.0).build());
        }},2);

        String json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isOk());

        inventoryResponse = findInventory(1);
        assertEquals(500, (int) inventoryResponse.getSugarCount());
    }


    @Test
    void test_order_withNonHotBeverageAndNonValidSugarAmount_returnIgnoreSugarSuccess() throws Exception {
        Gson gson = new Gson();
        InventoryResponse inventoryResponse = findInventory(1);
        assertNotNull(inventoryResponse);
        assertNotNull(inventoryResponse.getProducts());
        assertTrue(inventoryResponse.getProducts().size() > 0);
        Optional<InventoryProductResponse> optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getProductType() != ProductType.BEVERAGE_HOT).findAny();
        assertTrue(optionalProduct.isPresent());
        OrderRequest orderRequest = generateOrderRequest(optionalProduct.get().getId(),1,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(2).value(5.0).build());
        }},7);

        String json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isOk());

        inventoryResponse = findInventory(1);
        assertEquals(500, (int) inventoryResponse.getSugarCount());
    }


    @Test
    void test_order_withCashPaymentAndMoneyIsLessThanPrice_returnErrorMessege() throws Exception {
        Gson gson = new Gson();

        OrderRequest orderRequest = generateOrderRequest(1,10,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(1).value(5.0).build());
        }});

        String json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }


    @Test
    void test_order_withCashPaymentAndInventoryHasNotSufficientForChange_returnErrorMessage() throws Exception {
        Gson gson = new Gson();
        OrderRequest orderRequest = generateOrderRequest(1,3,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(3).value(200.0).build());
        }});

        String json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 2).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Inventory of Money change is not enough for payment"));
    }

    @Test
    void test_order_withCreditCardPaymentAndInvalidPin_returnErrorMessage() throws Exception {
        Gson gson = new Gson();
        OrderRequest orderRequest = generateOrderRequest(1,3,PaymentType.CREDIT_CARD,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(1).value(25.0).build());
        }});
        orderRequest.setCreditCardPin(123123213);

        String json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Credit card pin should be entered or between 1000 and 9999"));
    }

    @Test
    void test_order_withoutCreditCardPaymentAndInvalidPin_returnSuccess() throws Exception {
        Gson gson = new Gson();
        OrderRequest orderRequest = generateOrderRequest(6,3,PaymentType.CREDIT_CARD_CONTACTLESS,null);
        orderRequest.setCreditCardPin(123123213);

        String json = gson.toJson(orderRequest);
        mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_order_withCashAndProperValues_returnSuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Gson gson = new Gson();
        int productAmountWillBeBought = 3;
        int productIdWillBeBought = 1;
        InventoryResponse inventoryResponse = findInventory(1);
        assertNotNull(inventoryResponse.getProducts());
        assertTrue(inventoryResponse.getProducts().size() > 0);
        Optional<InventoryProductResponse> optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getId() == productIdWillBeBought).findAny();
        assertTrue(optionalProduct.isPresent());
        int expectedProductAmount = optionalProduct.get().getAmount() - productAmountWillBeBought;
        OrderRequest orderRequest = generateOrderRequest(productIdWillBeBought,productAmountWillBeBought,PaymentType.CASH,new ArrayList<Money>() {{
            add(Money.builder().moneyType(MoneyType.BANK_NOTE).amount(1).value(25.0).build());
        }});
        Inventory inventory = inventoryService.findById(1).orElse(null);
        assertNotNull(inventory);
        int firstMoneyOfInventoryCount = inventory.getMoneyList().stream().mapToInt(Money::getAmount).sum();
        String json = gson.toJson(orderRequest);
        MvcResult mvcResult = mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        OrderResponse actualOrderResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponse.class);
        inventoryResponse = findInventory(1);
        optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getId() == productIdWillBeBought).findAny();
        if (expectedProductAmount == 0)
            assertFalse(optionalProduct.isPresent());
        else{
            optionalProduct.ifPresent(inventoryProductResponse -> assertEquals(expectedProductAmount, inventoryProductResponse.getAmount()));
        }
        Inventory inventory2 = inventoryService.findById(1).orElse(null);
        assertNotNull(inventory2);
        int secondMoneyOfInventoryCount = inventory2.getMoneyList().stream().mapToInt(Money::getAmount).sum();
        int countOfChange = actualOrderResponseBody.getChanges().stream().mapToInt(Money::getAmount).sum();
        assertEquals(countOfChange,firstMoneyOfInventoryCount-secondMoneyOfInventoryCount);
    }

    @Test
    void test_order_withCreditCardAndProperValues_returnSuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Gson gson = new Gson();
        int productAmountWillBeBought = 3;
        int productIdWillBeBought = 2;
        InventoryResponse inventoryResponse = findInventory(1);
        assertNotNull(inventoryResponse.getProducts());
        assertTrue(inventoryResponse.getProducts().size() > 0);
        Optional<InventoryProductResponse> optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getId() == productIdWillBeBought).findAny();
        assertTrue(optionalProduct.isPresent());
        int expectedProductAmount = optionalProduct.get().getAmount() - productAmountWillBeBought;
        OrderRequest orderRequest = generateOrderRequest(productIdWillBeBought,productAmountWillBeBought,PaymentType.CREDIT_CARD,null);
        orderRequest.setCreditCardPin(1221);
        Inventory inventory = inventoryService.findById(1).orElse(null);
        assertNotNull(inventory);
        int firstMoneyOfInventoryCount = inventory.getMoneyList().stream().mapToInt(Money::getAmount).sum();
        String json = gson.toJson(orderRequest);
        MvcResult mvcResult = mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        OrderResponse actualOrderResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponse.class);
        inventoryResponse = findInventory(1);
        optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getId() == productIdWillBeBought).findAny();
        if (expectedProductAmount == 0)
            assertFalse(optionalProduct.isPresent());
        else{
            optionalProduct.ifPresent(inventoryProductResponse -> assertEquals(expectedProductAmount, inventoryProductResponse.getAmount()));
        }
    }

    @Test
    void test_order_withCreditCardContactlessAndProperValues_returnSuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Gson gson = new Gson();
        int productAmountWillBeBought = 3;
        int productIdWillBeBought = 5;
        InventoryResponse inventoryResponse = findInventory(1);
        assertNotNull(inventoryResponse.getProducts());
        assertTrue(inventoryResponse.getProducts().size() > 0);
        Optional<InventoryProductResponse> optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getId() == productIdWillBeBought).findAny();
        assertTrue(optionalProduct.isPresent());
        int expectedProductAmount = optionalProduct.get().getAmount() - productAmountWillBeBought;
        OrderRequest orderRequest = generateOrderRequest(productIdWillBeBought,productAmountWillBeBought,PaymentType.CREDIT_CARD_CONTACTLESS,null);
        Inventory inventory = inventoryService.findById(1).orElse(null);
        assertNotNull(inventory);
        int firstMoneyOfInventoryCount = inventory.getMoneyList().stream().mapToInt(Money::getAmount).sum();
        String json = gson.toJson(orderRequest);
        MvcResult mvcResult = mockMvc.perform(
                post("/orders/inventories/{invId}", 1).contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        OrderResponse actualOrderResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponse.class);
        inventoryResponse = findInventory(1);
        optionalProduct = inventoryResponse.getProducts().stream().filter(p -> p.getId() == productIdWillBeBought).findAny();
        if (expectedProductAmount == 0)
            assertFalse(optionalProduct.isPresent());
        else{
            optionalProduct.ifPresent(inventoryProductResponse -> assertEquals(expectedProductAmount, inventoryProductResponse.getAmount()));
        }
    }

    private InventoryResponse findInventory(int inventoryId) throws Exception {
        Gson gson = new Gson();
        MvcResult mvcResult = mockMvc.perform(
                get("/inventories/{inventoryId}", inventoryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String invResponse = mvcResult.getResponse().getContentAsString();
        InventoryResponse inventoryResponse = gson.fromJson(invResponse, InventoryResponse.class);
        assertNotNull(inventoryResponse);
        return inventoryResponse;
    }


    private OrderRequest generateOrderRequest(int productId, int productAmount, PaymentType paymentType, List<Money> loadedMoney){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(productId);
        orderRequest.setProductAmount(productAmount);
        orderRequest.setPaymentType(paymentType);
        if (loadedMoney != null)
            orderRequest.setLoadedMoneyList(loadedMoney);
        return orderRequest;
    }



    private OrderRequest generateOrderRequest(int productId, int productAmount, PaymentType paymentType, List<Money> loadedMoney,int sugar){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(productId);
        orderRequest.setProductAmount(productAmount);
        orderRequest.setPaymentType(paymentType);
        orderRequest.setLoadedMoneyList(loadedMoney);
        orderRequest.setProductSugarAmount(sugar);
        return orderRequest;
    }

}
