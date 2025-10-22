package org.example.productservice.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse implements Serializable {
    String id;
    String name;
    String description;
    BigDecimal price;
    Set<String> images;
    Integer stock;
    CategoryResponse category;
}
