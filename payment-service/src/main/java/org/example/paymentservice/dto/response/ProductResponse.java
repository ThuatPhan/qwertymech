package org.example.paymentservice.dto.response;

import java.math.BigDecimal;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String description;
    BigDecimal price;
    Set<String> images;
    Integer stock;
    CategoryResponse category;
}
