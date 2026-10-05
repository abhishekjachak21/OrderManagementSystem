package com.example.orderservice.service;

import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.*;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderResponse createOrder(
            CreateOrderRequest request) {

        // 1. Get product
        ProductResponse product =
                productClient.getProduct(request.productId());

        // 2. Check stock
        boolean available =
                productClient.checkAvailability(
                        request.productId(),
                        request.quantity()
                );

        if (!available) {
            throw new RuntimeException(
                    "Insufficient stock"
            );
        }

        // 3. Calculate amount
        BigDecimal amount =
                product.price()
                        .multiply(
                                BigDecimal.valueOf(
                                        request.quantity()
                                )
                        );

        // 4. Create initial order
        Order order = new Order();

        order.setProductId(request.productId());
        order.setQuantity(request.quantity());
        order.setAmount(amount);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder =
                orderRepository.save(order);

        // 5. Process payment
        PaymentResponse payment =
                paymentClient.processPayment(
                        new PaymentRequest(
                                savedOrder.getId(),
                                amount,
                                request.paymentMethod()
                        )
                );

        // 6. Handle payment result
        if (payment.status() == PaymentStatus.FAILED) {

            savedOrder.setStatus(
                    OrderStatus.PAYMENT_FAILED
            );

            savedOrder.setPaymentId(
                    payment.paymentId()
            );

            orderRepository.save(savedOrder);

            return toResponse(savedOrder);
        }

        // 7. Reduce stock
        productClient.reduceStock(
                request.productId(),
                request.quantity()
        );

        // 8. Confirm order
        savedOrder.setStatus(
                OrderStatus.CONFIRMED
        );

        savedOrder.setPaymentId(
                payment.paymentId()
        );

        Order finalOrder =
                orderRepository.save(savedOrder);

        return toResponse(finalOrder);
    }

    private OrderResponse toResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getAmount(),
                order.getStatus(),
                order.getPaymentId()
        );
    }
}
