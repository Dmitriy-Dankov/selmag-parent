package com.example.catalogueservice.service;

import com.example.catalogueservice.entity.Product;
import com.example.catalogueservice.payload.NewProductPayload;
import com.example.catalogueservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()) {
            return productRepository.findAllProductsByFilter("%" + filter + "%");
        }
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product createProduct(NewProductPayload payload) {
        return productRepository.save(new Product(null, payload.title(), payload.details()));
    }

    @Override
    public Optional<Product> findProduct(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
