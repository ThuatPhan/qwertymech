package org.example.productservice.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @Size(min = 5, max = 255, message = "Product name must be between 5 and 255 characters")
    String name;

    String description;

    @PositiveOrZero(message = "Product price can't be negative")
    BigDecimal price;

    Set<@NotBlank(message = "Each image URL can't be blank") String> images;

    @PositiveOrZero(message = "Product stock can't be negative")
    Integer stock;

    @UUID(message = "Invalid UUID format")
    String category;
}
