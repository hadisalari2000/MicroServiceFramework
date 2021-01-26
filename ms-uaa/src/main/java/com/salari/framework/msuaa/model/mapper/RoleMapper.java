package com.salari.framework.msuaa.model.mapper;
import com.salari.framework.msuaa.model.entity.Role;
import com.salari.framework.msuaa.model.dto.role.RoleDTO;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleDTO ROLE_DTO(Role role);
}
