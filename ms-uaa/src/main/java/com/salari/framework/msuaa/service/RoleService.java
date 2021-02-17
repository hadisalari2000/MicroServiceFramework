package com.salari.framework.msuaa.service;
import com.salari.framework.common.handler.exception.DuplicateException;
import com.salari.framework.common.handler.exception.NotFoundException;
import com.salari.framework.msuaa.model.domain.role.RoleAddRequest;
import com.salari.framework.msuaa.model.domain.role.RoleChangeActivationRequest;
import com.salari.framework.msuaa.model.domain.role.RoleEditRequest;
import com.salari.framework.common.model.base.BaseDTO;
import com.salari.framework.msuaa.model.entity.Role;
import com.salari.framework.msuaa.model.entity.User;
import com.salari.framework.msuaa.model.enums.RoleTypes;
import com.salari.framework.msuaa.model.mapper.RoleMapper;
import com.salari.framework.msuaa.repository.RoleRepository;
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
        Role role= getRole(id);
        return BaseDTO.builder().data(roleMapper.ROLE_DTO(role)).build();
    }

    public BaseDTO getByKey(String key){
        Role role=roleRepository.findByKey(key).orElseThrow(()->
                NotFoundException.getInstance(Role.class,"key",key));
        return BaseDTO.builder().data(roleMapper.ROLE_DTO(role)).build();
    }

    public BaseDTO getAllByActivation(Boolean active){

        List<Role> roles=roleRepository.findAllByActive(active).orElseThrow(()->
                NotFoundException.getInstance(Role.class,"active",active.toString()));

        return BaseDTO.builder().data(roles.stream().map(roleMapper::ROLE_DTO)).build();
    }

    public BaseDTO getUserRoles(Integer userId){
        List<Role> roles=roleRepository.findAllByUsers_Id(userId).orElseThrow(()->
                NotFoundException.getInstance(Role.class,"user-id",userId.toString()));

        return BaseDTO.builder().data(roles.stream().map(roleMapper::ROLE_DTO)).build();
    }

    public Role getLastUserRole(User user) {
        List<Role> userRoles =roleRepository.findAllByUsers_Id(user.getId()).orElseThrow(()->
                NotFoundException.getInstance(Role.class,"user-id",user.getId().toString()));

        Role lastUserRole=null;
         if(userRoles.stream().findFirst().isPresent())
             lastUserRole=userRoles.stream().findFirst().get();

        if (user.getCurrentRoleId() != null) {
            lastUserRole = userRoles.stream().filter(uRole -> uRole.getId().equals(user.getCurrentRoleId())).findFirst().orElse(lastUserRole);
        }
        return lastUserRole;
    }

    public BaseDTO getAllByRoleType(RoleTypes roleType){
        List<Role> roles=roleRepository.findAllByRoleType(roleType).orElseThrow(()->
                NotFoundException.getInstance(Role.class,"role-type",roleType.toString()));

        return BaseDTO.builder().data(roles.stream().map(roleMapper::ROLE_DTO)).build();
    }

    public BaseDTO add(RoleAddRequest request){
        checkDuplicateRole(request.getTitle(),request.getKey(),null);

        Role role=Role.builder()
                .roleType(request.getRoleType())
                .description(request.getDescription())
                .key(request.getKey())
                .title(request.getTitle())
                .build();

        roleRepository.save(role);

        return BaseDTO.builder().data(roleMapper.ROLE_DTO(role)).build();
    }

    public BaseDTO edit(RoleEditRequest request){
        Role role= getRole(request.getId());
        checkDuplicateRole(request.getTitle(),request.getKey(),role);
        role.setDescription(request.getDescription());
        role.setKey(request.getKey());
        role.setRoleType(request.getRoleType());
        role.setTitle(request.getTitle());
        roleRepository.save(role);
        return BaseDTO.builder().data(roleMapper.ROLE_DTO(role)).build();
    }

    public BaseDTO delete(Integer id){
        Role role= getRole(id);
        roleRepository.delete(role);
        return BaseDTO.builder().data(null).build();
    }
    
    public BaseDTO changeActivation(RoleChangeActivationRequest request){
        Role role= getRole(request.getId());
        role.setActive(request.getActive());
        roleRepository.save(role);
        return BaseDTO.builder().data(roleMapper.ROLE_DTO(role)).build();
    }

    private Role getRole(Integer roleId){
        return roleRepository.findById(roleId).orElseThrow(()->
                NotFoundException.getInstance(Role.class,"id",roleId.toString()));
    }

    private void checkDuplicateRole(String title,String key,Role role){

        Optional<Role> roleExist=roleRepository.findByTitleOrKey(title,key);

        if(roleExist.isPresent() && !(role!=null && roleExist.get().getId().equals(role.getId())) && roleExist.get().getKey().equalsIgnoreCase(key))
            throw DuplicateException.getInstance(Role.class, "key",key);

        if(roleExist.isPresent() && !(role!=null && roleExist.get().getId().equals(role.getId())) && roleExist.get().getTitle().equalsIgnoreCase(title))
            throw DuplicateException.getInstance(Role.class, "title",title);
    }
}
