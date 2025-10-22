package org.example.identityservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreationRequest {
    @Size(max = 30, message = "Name can't longer than 30 characters")
    @NotBlank(message = "Name can't be blank")
    String name;

    @Size(max = 50, message = "Description can't longer than 50 characters")
    String description;
}
