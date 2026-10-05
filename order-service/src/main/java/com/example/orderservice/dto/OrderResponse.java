package com.example.orderservice.dto;

import com.example.orderservice.entity.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        Long productId,
        Integer quantity,
        BigDecimal amount,
        OrderStatus status,
        Long paymentId
) {
}