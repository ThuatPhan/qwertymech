package org.example.identityservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Email(message = "Not valid email format")
    String email;

    @Size(min = 8, max = 20, message = "Password can't be longer than 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&;])[A-Za-z\\d@$!%*?&;]{8,20}$",
            message =
                    "Password must be 8-20 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    String password;

    @Size(max = 255, message = "First name can't be longer than 255 characters")
    String firstName;

    @Size(max = 255, message = "Last name can't be longer than 255 characters")
    String lastName;
}
