package com.salari.framework.uaa.service;
import com.salari.framework.uaa.repository.TemplatePermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class TemplatePermissionService {

    private final TemplatePermissionRepository templatePermissionRepository;

    public TemplatePermissionService(TemplatePermissionRepository templatePermissionRepository) {
        this.templatePermissionRepository = templatePermissionRepository;
    }
}
