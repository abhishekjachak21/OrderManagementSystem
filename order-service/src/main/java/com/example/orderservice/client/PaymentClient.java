package com.example.orderservice.client;

import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.dto.PaymentResponse;
import com.example.orderservice.dto.ProductResponse;
import com.example.orderservice.dto.StockUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(System.getenv().getOrDefault(
                        "PAYMENT_SERVICE_URL",
                        "http://localhost:8083"
                ))
                .build();
    }

    public PaymentResponse processPayment(
            PaymentRequest request) {

        return restClient.post()
                .uri("/payments")
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);
    }
}
