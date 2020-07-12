package com.odeal.vendingmachine.products.data;

import com.odeal.vendingmachine.products.dtos.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class ProductDataServiceTest {

    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private ModelMapper modelMapper = new ModelMapper();
    private ProductDataService productDataService;

    @BeforeEach
    void setUp() {
        this.productDataService = new ProductDataService(productRepository,modelMapper);
    }

    @Test
    void test_saveProduct_withProperProduct_shouldBeSaved() {
        Product product = Product.builder().name("A").price(1.5).build();
        ProductEntity productEntity = ProductEntity.builder().name("A").price(1.5).build();
        ProductEntity savedProductEntity = savedProductEntity();
        when(productRepository.save(productEntity)).thenReturn(savedProductEntity);
        Product savedProduct = productDataService.save(product);
        assertEquals(savedProduct.getId(),savedProductEntity.getId());
        assertEquals(savedProduct.getName(),product.getName());
        assertEquals(savedProduct.getPrice(),product.getPrice());
    }


    @Test
    void test_saveAllProduct_withProperProducts_shouldBeSaved() {
        List<Product> products = productsWithoutId();
        List<Product> expectedProducts = productsWithId();
        List<ProductEntity> productEntities = productEntities();
        List<ProductEntity> savedProductEntites = savedProductEntities();
        when(productRepository.saveAll(productEntities)).thenReturn(savedProductEntites);
        List<Product> savedProduct = productDataService.saveAll(products);
        assertEquals(expectedProducts,savedProduct);
    }


    @Test
    void test_findProductById_withIdOfProduct_shouldBeFound() {
        Product expectedProduct = productWithId();
        ProductEntity productEntity = savedProductEntity();
        when(productRepository.findById(1)).thenReturn(Optional.of(productEntity));
        Optional<Product> product = productDataService.findById(1);
        assertTrue(product.isPresent());
        assertEquals(expectedProduct,product.get());
    }

    private ProductEntity savedProductEntity(){
        return ProductEntity.builder()
                .id(1)
                .name("A")
                .price(1.5)
                .build();
    }

    private List<ProductEntity> savedProductEntities(){
        return new ArrayList<ProductEntity>(){{
           add(ProductEntity.builder()
                   .id(1)
                   .name("A")
                   .price(1.5)
                   .build());
            add(ProductEntity.builder()
                    .id(2)
                    .name("B")
                    .price(2.5)
                    .build());
        }};
    }

    private List<ProductEntity> productEntities(){
        return new ArrayList<ProductEntity>(){{
            add(ProductEntity.builder()
                    .name("A")
                    .price(1.5)
                    .build());
            add(ProductEntity.builder()
                    .name("B")
                    .price(2.5)
                    .build());
        }};
    }

    private List<Product> productsWithoutId(){
        return new ArrayList<Product>(){{
            add(Product.builder()
                    .name("A")
                    .price(1.5)
                    .build());
            add(Product.builder()
                    .name("B")
                    .price(2.5)
                    .build());
        }};
    }

    private List<Product> productsWithId(){
        return new ArrayList<Product>(){{
            add(Product.builder()
                    .id(1)
                    .name("A")
                    .price(1.5)
                    .build());
            add(Product.builder()
                    .id(2)
                    .name("B")
                    .price(2.5)
                    .build());
        }};
    }


    private Product productWithId(){
        return Product.builder()
                    .id(1)
                    .name("A")
                    .price(1.5)
                    .build();
    }


}