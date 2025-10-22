package org.example.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreationRequest {
    @Size(min = 5, max = 100, message = "Category name can't be longer than 100 characters")
    @NotBlank(message = "Name can't be blank")
    String name;

    String image;
}
