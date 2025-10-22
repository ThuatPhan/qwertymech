package org.example.notificationservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

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
    String shippingAddress;
    Set<OrderItemResponse> items;
    Instant createdAt;
    Instant updatedAt;
}
