package com.salari.framework.msuaa.repository;

import com.salari.framework.msuaa.model.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission,Integer> {

    Optional<List<RolePermission>> findAllByRoleId(Integer roleId);

}
