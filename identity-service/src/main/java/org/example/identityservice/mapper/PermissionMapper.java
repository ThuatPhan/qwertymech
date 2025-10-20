package org.example.identityservice.mapper;

import org.example.identityservice.dto.request.PermissionCreationRequest;
import org.example.identityservice.dto.request.PermissionUpdateRequest;
import org.example.identityservice.dto.response.PermissionResponse;
import org.example.identityservice.entity.Permission;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePermission(@MappingTarget Permission permission, PermissionUpdateRequest request);
}
