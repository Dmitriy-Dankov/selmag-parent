package com.example.catalogueservice.controller;

import com.example.catalogueservice.entity.Product;
import com.example.catalogueservice.payload.NewProductPayload;
import com.example.catalogueservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {
    private final ProductService productService;

    @GetMapping
    public Iterable<Product> findProducts(@RequestParam(value = "filter", required = false) String filter) {
        return productService.findAllProducts(filter);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Product product = productService.createProduct(payload);
        return ResponseEntity
                .created(uriBuilder.pathSegment("{productId}")
                        .build(Map.of("productId", product.getId())))
                .body(product);
    }
}
