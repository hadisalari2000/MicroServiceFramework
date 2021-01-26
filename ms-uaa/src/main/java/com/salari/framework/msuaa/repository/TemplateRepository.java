package com.salari.framework.msuaa.repository;

import com.salari.framework.msuaa.model.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template,Integer> {

}
