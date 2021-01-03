package com.salari.framework.uaa.service;
import com.salari.framework.uaa.repository.RoleTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleTemplateService {

    private final RoleTemplateRepository roleTemplateRepository;

    public RoleTemplateService(RoleTemplateRepository roleTemplateRepository) {
        this.roleTemplateRepository = roleTemplateRepository;
    }
}
