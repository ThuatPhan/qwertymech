package org.example.orderservice.controller;

import org.example.event.dto.OrderEvent;
import org.example.orderservice.enums.PaymentStatus;
import org.example.orderservice.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderEventController {
    OrderService orderService;

    @KafkaListener(topics = "order-delivery")
    public void listenOrderEvent(OrderEvent event) {
        Long orderId = Long.valueOf(event.getParam().get("orderId").toString());
        String status = event.getParam().get("status").toString();

        orderService.markOrderAsPaid(orderId, status.equals("success") ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
    }
}
