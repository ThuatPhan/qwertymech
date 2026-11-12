package org.example.userservice.controller;

import org.example.common.dto.ApiResponse;
import org.example.userservice.dto.response.UserResponse;
import org.example.userservice.service.UserService;
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
