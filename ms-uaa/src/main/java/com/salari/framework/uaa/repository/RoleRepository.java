package com.salari.framework.uaa.repository;

import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.enums.RoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findById(Integer id);
    List<Role> findAllByActive(Boolean active);
    List<Role> findAllByRoleType(RoleTypes roleTypes);
    List<Role> findAllByUsers_Id(Integer userId);
}
