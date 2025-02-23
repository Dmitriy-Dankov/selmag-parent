package com.example.catalogueservice.service;

import com.example.catalogueservice.entity.Product;
import com.example.catalogueservice.payload.NewProductPayload;

import java.util.Optional;

public interface ProductService {
    Iterable<Product> findAllProducts(String filter);

    Product createProduct(NewProductPayload payload);

    Optional<Product> findProduct(Integer id);

    void updateProduct(Product product);

    void deleteProduct(Integer id);
}
