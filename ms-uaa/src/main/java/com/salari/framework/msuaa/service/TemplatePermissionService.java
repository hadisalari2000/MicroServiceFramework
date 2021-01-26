package com.salari.framework.msuaa.service;
import com.salari.framework.msuaa.repository.TemplatePermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class TemplatePermissionService {

    private final TemplatePermissionRepository templatePermissionRepository;

    public TemplatePermissionService(TemplatePermissionRepository templatePermissionRepository) {
        this.templatePermissionRepository = templatePermissionRepository;
    }
}
