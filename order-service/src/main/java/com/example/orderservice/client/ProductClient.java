package com.example.orderservice.client;

import com.example.orderservice.dto.ProductResponse;
import com.example.orderservice.dto.StockUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(System.getenv().getOrDefault(
                        "PRODUCT_SERVICE_URL",
                        "http://localhost:8082"
                ))
                .build();
    }


    public ProductResponse getProduct(Long productId) {

        return restClient.get()
                .uri("/products/{id}", productId)
                .retrieve()
                .body(ProductResponse.class);
    }

    public boolean checkAvailability(
            Long productId,
            Integer quantity) {

        return Boolean.TRUE.equals(
                restClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/products/{id}/availability")
                                .queryParam("quantity", quantity)
                                .build(productId))
                        .retrieve()
                        .body(Boolean.class)
        );
    }

    public void reduceStock(
            Long productId,
            Integer quantity) {

        restClient.put()
                .uri("/products/{id}/stock", productId)
                .body(new StockUpdateRequest(quantity))
                .retrieve()
                .toBodilessEntity();
    }
}