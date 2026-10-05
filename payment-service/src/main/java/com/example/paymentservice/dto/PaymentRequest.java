package com.example.paymentservice.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod
) {
}