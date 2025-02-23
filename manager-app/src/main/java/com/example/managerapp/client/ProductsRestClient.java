package com.example.managerapp.client;

import com.example.managerapp.entity.Product;
import com.example.managerapp.payload.NewProductPayload;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {
    List<Product> findAllProducts(String filter);

    Optional<Product> findProduct(Integer id);

    Product createProduct(NewProductPayload payload);

    void updateProduct(Product product);

    void deleteProduct(Integer id);
}
