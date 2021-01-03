package com.salari.framework.uaa.service;
import com.salari.framework.uaa.repository.TemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }
}
