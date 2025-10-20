package org.example.identityservice.dto.request;

import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreationRequest {
    @Max(value = 30, message = "Name can't longer than 30 characters")
    @NotBlank(message = "Name can't be blank")
    String name;

    @Min(value = 1, message = "Role must have at least one permission")
    Set<@NotBlank(message = "Each permission must have a name") String> permissions;
}
