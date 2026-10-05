package com.example.orderservice.dto;

import com.example.orderservice.entity.PaymentMethod;

public record CreateOrderRequest(
        Long productId,
        Integer quantity,
        PaymentMethod paymentMethod
) {
}