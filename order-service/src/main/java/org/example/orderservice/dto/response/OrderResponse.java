package org.example.orderservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import org.example.orderservice.enums.OrderStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    BigDecimal totalPrice;
    OrderStatus status;
    String shippingAddress;
    Set<OrderItemResponse> items;
    Instant createdAt;
    Instant updatedAt;
}
