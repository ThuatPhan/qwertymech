package org.example.identityservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.example.identityservice.dto.request.PermissionCreationRequest;
import org.example.identityservice.dto.request.PermissionUpdateRequest;
import org.example.identityservice.dto.response.ApiResponse;
import org.example.identityservice.dto.response.PermissionResponse;
import org.example.identityservice.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionCreationRequest request) {
        return ApiResponse.success(permissionService.createPermission(request));
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.success(permissionService.getAllPermissions());
    }

    @PutMapping("/{id}")
    public ApiResponse<PermissionResponse> updatePermission(
            @PathVariable String id, @RequestBody @Valid PermissionUpdateRequest request) {
        return ApiResponse.success(permissionService.updatePermission(id, request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePermission(@PathVariable String id) {
        permissionService.deletePermission(id);
    }
}
