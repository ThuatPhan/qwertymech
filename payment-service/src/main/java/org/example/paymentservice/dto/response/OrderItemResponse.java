package org.example.paymentservice.dto.response;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    ProductResponse product;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal subTotal;
}
