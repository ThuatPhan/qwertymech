package org.example.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemCreationRequest {
    @NotBlank(message = "Product can't be blank")
    String product;

    @Min(value = 1, message = "Quantity can't be less than 1")
    Integer quantity;
}
