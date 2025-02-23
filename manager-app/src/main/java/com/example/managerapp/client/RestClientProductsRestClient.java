package com.example.managerapp.client;

import com.example.managerapp.entity.Product;
import com.example.managerapp.payload.NewProductPayload;
import com.example.managerapp.payload.UpdateProductPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientProductsRestClient implements ProductsRestClient {
    private static final ParameterizedTypeReference<List<Product>> PRODUCT_TYPE_REFERENCE
            = new ParameterizedTypeReference<>() {
    }; // сообщает jeckson, что нужно преобразовать в список.

    private final RestClient restClient;

    @Override
    public List<Product> findAllProducts(String filter) {
        return restClient.get()
                .uri("catalogue-api/products?filter={filter}", filter)
                .retrieve().body(PRODUCT_TYPE_REFERENCE);
    }

    @Override
    public Optional<Product> findProduct(Integer id) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("catalogue-api/products/{productId}", id)
                    .retrieve()
                    .body(Product.class));
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        }
    }

    @Override
    public Product createProduct(NewProductPayload payload) {
        try {
            return restClient.post().uri("catalogue-api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest ex) {
            ProblemDetail problemDetail = ex.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            restClient.patch().uri("catalogue-api/products/{productId}", product.id())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateProductPayload(product.title(), product.details()))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest ex) {
            ProblemDetail problemDetail = ex.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProduct(Integer id) {
        try {
            restClient.delete()
                    .uri("catalogue-api/products/{productId}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NoSuchElementException(ex);
        }
    }
}
