package com.odeal.vendingmachine.products.data;

import com.odeal.vendingmachine.products.dtos.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductDataService {

    private final ProductRepository productRepository;
    private final ModelMapper productsModelMapper;

    public Product save(Product product) {
        ProductEntity productEntity = productsModelMapper.map(product, ProductEntity.class);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return productsModelMapper.map(savedProductEntity, Product.class);
    }

    public List<Product> saveAll(List<Product> products) {
        List<ProductEntity> productEntities = products.stream()
                .map(p -> productsModelMapper.map(p, ProductEntity.class))
                .collect(Collectors.toList());
        List<ProductEntity> savedProductEntities = productRepository.saveAll(productEntities);
        return savedProductEntities.stream()
                .map(p -> productsModelMapper.map(p, Product.class))
                .collect(Collectors.toList());
    }

    public Optional<Product> findById(int id){
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        return productEntity.map(p -> productsModelMapper.map(p,Product.class));
    }

    public ProductDataService(ProductRepository productRepository, ModelMapper productsModelMapper) {
        this.productRepository = productRepository;
        this.productsModelMapper = productsModelMapper;
    }
}
