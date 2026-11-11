package org.example.identityservice.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.example.identityservice.dto.request.RoleCreationRequest;
import org.example.identityservice.dto.request.RoleUpdateRequest;
import org.example.identityservice.dto.response.RoleResponse;
import org.example.identityservice.entity.Permission;
import org.example.identityservice.entity.Role;
import org.example.identityservice.exception.AppException;
import org.example.identityservice.exception.ErrorCode;
import org.example.identityservice.mapper.RoleMapper;
import org.example.identityservice.repository.PermissionRepository;
import org.example.identityservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleCreationRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        Role role = roleMapper.toRole(request);

        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
        }

        Role savedRole = roleRepository.save(role);

        return roleMapper.toRoleResponse(savedRole);
    }

    public RoleResponse getRole(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    public RoleResponse updateRole(String id, RoleUpdateRequest request) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        if (roleRepository.existsByNameAndIdNot(id, request.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
            role.getPermissions().clear();
            role.getPermissions().addAll(permissions);
        }

        roleMapper.updateRole(role, request);

        Role savedRole = roleRepository.save(role);

        return roleMapper.toRoleResponse(savedRole);
    }

    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        roleRepository.deleteById(id);
    }
}
