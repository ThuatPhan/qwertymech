package org.example.identityservice.mapper;

import org.example.identityservice.dto.request.RoleCreationRequest;
import org.example.identityservice.dto.request.RoleUpdateRequest;
import org.example.identityservice.dto.response.RoleResponse;
import org.example.identityservice.entity.Role;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PermissionMapper.class})
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);

    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateRole(@MappingTarget Role role, RoleUpdateRequest request);
}
