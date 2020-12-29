package com.salari.framework.uaa.service;

import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.domain.role.RoleAddRequest;
import com.salari.framework.uaa.model.domain.role.RoleChangeActivationRequest;
import com.salari.framework.uaa.model.domain.role.RoleEditRequest;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.mapper.RoleMapper;
import com.salari.framework.uaa.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public BaseDTO get(Integer id){
        Role role=getExistRole(id);
        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(roleMapper.ROLE_DTO(role))
                .build();
    }

    public BaseDTO add(RoleAddRequest request){
        if(roleRepository.findByTitleOrKey(request.getTitle(),request.getKey()).isPresent())
            throw ServiceException.getInstance("role-duplicated", HttpStatus.BAD_REQUEST);

        Role role=Role.builder()
                .roleType(request.getRoleType())
                .description(request.getDescription())
                .key(request.getKey())
                .title(request.getTitle())
                .build();

        roleRepository.save(role);

        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(roleMapper.ROLE_DTO(role))
                .build();
    }

    public BaseDTO edit(RoleEditRequest request){
        Role role=getExistRole(request.getId());

        Optional<Role> existRole=roleRepository.findByTitleOrKey(request.getTitle(),request.getKey());
        if(existRole.isPresent() && !existRole.get().getId().equals(request.getId()))
            throw ServiceException.getInstance("role-duplicated", HttpStatus.BAD_REQUEST);

        role.setDescription(request.getDescription());
        role.setKey(request.getKey());
        role.setRoleType(request.getRoleType());
        role.setTitle(request.getTitle());
        roleRepository.save(role);

        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(roleMapper.ROLE_DTO(role))
                .build();
    }

    public BaseDTO delete(Integer id){
        Role role=getExistRole(id);
        roleRepository.delete(role);
        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(null)
                .build();
    }
    
    public BaseDTO changeActivation(RoleChangeActivationRequest request){
        Role role=getExistRole(request.getId());
        role.setActive(request.getActive());
        roleRepository.save(role);

        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(roleMapper.ROLE_DTO(role))
                .build();
    }
    
    private Role getExistRole(Integer roleId){
        return roleRepository.findById(roleId)
                .orElseThrow(()-> ServiceException.getInstance("role-not-found", HttpStatus.NOT_FOUND));
    }
}
