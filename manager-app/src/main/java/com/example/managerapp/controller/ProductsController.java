package com.example.managerapp.controller;

import com.example.managerapp.client.BadRequestException;
import com.example.managerapp.client.ProductsRestClient;
import com.example.managerapp.entity.Product;
import com.example.managerapp.payload.NewProductPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/catalogue/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsRestClient productsRestClient;

    @GetMapping("list")
    public String getProductslist(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("products", productsRestClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload, Model model) {
        try {
            Product product = productsRestClient.createProduct(payload);
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException ex) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", ex.getErrors());
            return "catalogue/products/new_product";
        }
    }
}
