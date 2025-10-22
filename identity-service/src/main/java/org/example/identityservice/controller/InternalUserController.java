package org.example.identityservice.controller;

import org.example.identityservice.dto.response.ApiResponse;
import org.example.identityservice.dto.response.UserResponse;
import org.example.identityservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserController {
    UserService userService;

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getUser() {
        return ApiResponse.success(userService.getUser());
    }
}
