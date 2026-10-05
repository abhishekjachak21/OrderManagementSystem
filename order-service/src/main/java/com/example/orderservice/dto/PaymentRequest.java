package com.example.orderservice.dto;

import com.example.orderservice.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod
) {
}