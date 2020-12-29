package com.salari.framework.uaa.model.mapper;
import com.salari.framework.uaa.model.dto.role.RoleDTO;
import com.salari.framework.uaa.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleDTO ROLE_DTO(Role role);
}
