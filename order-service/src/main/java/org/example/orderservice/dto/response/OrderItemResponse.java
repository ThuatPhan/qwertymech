package org.example.orderservice.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse implements Serializable {
    ProductResponse product;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal subTotal;
}
