package com.salari.framework.uaa.service;
import com.salari.framework.uaa.repository.RolePermissionRepository;
import org.springframework.stereotype.Service;


@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

}
