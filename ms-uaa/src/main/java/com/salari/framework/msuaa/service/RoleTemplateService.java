package com.salari.framework.msuaa.service;
import com.salari.framework.msuaa.repository.RoleTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleTemplateService {

    private final RoleTemplateRepository roleTemplateRepository;

    public RoleTemplateService(RoleTemplateRepository roleTemplateRepository) {
        this.roleTemplateRepository = roleTemplateRepository;
    }
}
