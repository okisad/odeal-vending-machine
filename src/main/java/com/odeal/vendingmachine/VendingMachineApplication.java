package com.odeal.vendingmachine;

import com.odeal.vendingmachine.inventories.data.InventoryDataService;
import com.odeal.vendingmachine.inventories.dtos.Inventory;
import com.odeal.vendingmachine.payments.dtos.Money;
import com.odeal.vendingmachine.payments.dtos.MoneyType;
import com.odeal.vendingmachine.products.data.ProductDataService;
import com.odeal.vendingmachine.products.dtos.Product;
import com.odeal.vendingmachine.products.utils.RandomProductGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableSwagger2
public class VendingMachineApplication implements CommandLineRunner {

    @Autowired
    private InventoryDataService inventoryDataService;
    @Autowired
    private ProductDataService productDataService;

    private static final int COUNT_OF_ITEMS = 3;


    public static void main(String[] args) {
        SpringApplication.run(VendingMachineApplication.class, args);
/*        Scanner scanner = new Scanner(System.in);

        //  prompt for the user's name
        System.out.print("Enter your name: ");

        // get their input as a String
        String username = scanner.next();

        System.out.println(username);*/
    }

    @Override
    public void run(String... args) {
        List<Product> productList = RandomProductGenerator.generate(5, 5, 20);
        productList = productDataService.saveAll(productList);
        run(1000,productList,"Inventory 1");
        run(1,productList,"Inventory 2");

    }

    public void  run(int amountOfEachMoney,List<Product> productList,String invName){
        List<Product> products = new ArrayList<>();
        List<Money> moneyList = generateMoneys(amountOfEachMoney);
        productList.forEach(p -> products.addAll(duplicate(p)));
        Inventory inventory = Inventory.builder()
                .name(invName)
                .products(products)
                .sugarCount(500)
                .moneyList(moneyList)
                .build();
        inventoryDataService.save(inventory);
        //moneyDataService.saveAll(generateMoneys(amountOfEachMoney));
    }

    public List<Product> duplicate(Product p) {
        List<Product> products = new ArrayList<>();
        IntStream.range(0, COUNT_OF_ITEMS).forEach(i -> {
            products.add(Product.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .type(p.getType())
                    .price(p.getPrice()).build()
            );
        });
        return products;
    }


    private List<Money> generateMoneys(int amount){
        return new ArrayList<Money>(){{
            add(Money.builder().value(0.05).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(0.10).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(0.25).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(0.50).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(1.00).moneyType(MoneyType.COIN).amount(amount).build());
            add(Money.builder().value(5.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(10.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(20.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(50.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(100.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
            add(Money.builder().value(200.00).moneyType(MoneyType.BANK_NOTE).amount(amount).build());
        }};
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
