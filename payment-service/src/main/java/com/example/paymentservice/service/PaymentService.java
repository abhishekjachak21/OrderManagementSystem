package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentMethod;
import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.dto.PaymentStatus;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponse processPayment(PaymentRequest request) {

        Payment payment = new Payment();

        payment.setOrderId(request.orderId());
        payment.setAmount(request.amount());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setCreatedAt(LocalDateTime.now());

        if (request.paymentMethod() == PaymentMethod.FAIL) {

            payment.setStatus(PaymentStatus.FAILED);

            payment.setTransactionId(null);

        } else {

            payment.setStatus(PaymentStatus.SUCCESS);

            payment.setTransactionId("TXN-" + UUID.randomUUID());

        }

        Payment saved = paymentRepository.save(payment);

        return new PaymentResponse(
                saved.getId(),
                saved.getOrderId(),
                saved.getAmount(),
                saved.getStatus(),
                saved.getTransactionId()
        );
    }

    public PaymentResponse getPaymentByOrderId(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getTransactionId()
        );
    }

}