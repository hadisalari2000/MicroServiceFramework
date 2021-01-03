package com.salari.framework.uaa.repository;

import com.salari.framework.uaa.model.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template,Integer> {

}
