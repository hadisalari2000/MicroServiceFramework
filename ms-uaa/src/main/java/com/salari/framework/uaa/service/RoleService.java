package com.salari.framework.uaa.service;
import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.domain.role.*;
import com.salari.framework.uaa.model.dto.base.*;
import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.RoleTypes;
import com.salari.framework.uaa.model.mapper.RoleMapper;
import com.salari.framework.uaa.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public BaseDTO getByKey(String key){
        Role role=roleRepository.findByKey(key)
                .orElseThrow(()->ServiceException.getInstance("role-not-found",HttpStatus.NOT_FOUND));
        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(roleMapper.ROLE_DTO(role))
                .build();
    }

    public BaseDTO getAllByActivation(Boolean active){

        List<Role> roles=roleRepository.findAllByActive(active)
                .orElseThrow(()->ServiceException.getInstance("role-not-found",HttpStatus.NOT_FOUND));

        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(roles.stream().map(roleMapper::ROLE_DTO))
                .build();
    }

    public BaseDTO getUserRoles(Integer userId){
        List<Role> roles=roleRepository.findAllByUsers_Id(userId)
                .orElseThrow(()->ServiceException.getInstance("role-not-found",HttpStatus.NOT_FOUND));

        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(roles.stream().map(roleMapper::ROLE_DTO))
                .build();
    }

    public Role getLastUserRole(User user) {
        List<Role> userRoles =roleRepository.findAllByUsers_Id(user.getId())
                .orElseThrow(()->ServiceException.getInstance("role-not-found",HttpStatus.NOT_FOUND));

        Role lastUserRole=null;
         if(userRoles.stream().findFirst().isPresent())
             lastUserRole=userRoles.stream().findFirst().get();

        if (user.getCurrentRoleId() != null) {
            lastUserRole = userRoles.stream().filter(uRole -> uRole.getId().equals(user.getCurrentRoleId())).findFirst().orElse(lastUserRole);
        }
        return lastUserRole;
    }

    public BaseDTO getAllByRoleType(RoleTypes roleType){
        List<Role> roles=roleRepository.findAllByRoleType(roleType)
                .orElseThrow(()->ServiceException.getInstance("role-not-found",HttpStatus.NOT_FOUND));

        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(roles.stream().map(roleMapper::ROLE_DTO))
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
