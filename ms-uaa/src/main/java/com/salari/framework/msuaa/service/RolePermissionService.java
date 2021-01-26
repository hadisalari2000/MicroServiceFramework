package com.salari.framework.msuaa.service;
import com.salari.framework.msuaa.repository.RolePermissionRepository;
import org.springframework.stereotype.Service;


@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

}
