package org.example.identityservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.example.identityservice.dto.request.RoleCreationRequest;
import org.example.identityservice.dto.request.RoleUpdateRequest;
import org.example.identityservice.dto.response.ApiResponse;
import org.example.identityservice.dto.response.RoleResponse;
import org.example.identityservice.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleCreationRequest request) {
        return ApiResponse.success(roleService.createRole(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRole(@PathVariable String id) {
        return ApiResponse.success(roleService.getRole(id));
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.success(roleService.getAllRoles());
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable String id, @RequestBody @Valid RoleUpdateRequest request) {
        return ApiResponse.success(roleService.updateRole(id, request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
    }
}
