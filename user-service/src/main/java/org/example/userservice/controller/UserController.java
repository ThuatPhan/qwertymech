package org.example.userservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.example.common.dto.ApiResponse;
import org.example.userservice.dto.request.UserCreationRequest;
import org.example.userservice.dto.request.UserUpdateRequest;
import org.example.userservice.dto.response.UserResponse;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    @GetMapping("/info/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        return ApiResponse.success(userService.getUser(id));
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.success(userService.getUser());
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }

    @PutMapping
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.success(userService.updateUser(request));
    }
}
