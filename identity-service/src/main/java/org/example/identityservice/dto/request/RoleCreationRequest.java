package org.example.identityservice.dto.request;

import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreationRequest {
    @Size(max = 30, message = "Name can't longer than 30 characters")
    @NotBlank(message = "Name can't be blank")
    String name;

    @Size(min = 1, message = "Role must have at least one permission")
    Set<@NotBlank(message = "Each permission must have a name") String> permissions;
}
