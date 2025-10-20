package org.example.identityservice.dto.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String firstName;
    String lastName;
    String avatar;
    Set<RoleResponse> roles;
}
