package com.example.managerapp.controller;

import com.example.managerapp.client.BadRequestException;
import com.example.managerapp.client.ProductsRestClient;
import com.example.managerapp.entity.Product;
import com.example.managerapp.payload.UpdateProductPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/catalogue/products/{productId:\\d+}")
@RequiredArgsConstructor
public class ProductController {
    private final ProductsRestClient productsRestClient;
    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") Integer id) {
        return productsRestClient.findProduct(id)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public String getProductPage(@PathVariable("productId") Integer id, Model model) {
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage()  {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute(value = "product", binding = false) Product product,
                                UpdateProductPayload payload, Model model) {
        try {
            productsRestClient.updateProduct(new Product(product.id(), payload.title(), payload.details()));
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException ex){
            model.addAttribute("payload", payload);
            model.addAttribute("errors", ex.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("delete")
    public String updateProduct(@ModelAttribute("product") Product product)  {
        productsRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException e, Model model, Locale locale) {
        model.addAttribute("error",
                messageSource.getMessage(e.getMessage(), null, locale));
        return "errors/404";
    }
}
