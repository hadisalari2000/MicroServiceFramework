package com.salari.framework.msuaa.repository;

import com.salari.framework.msuaa.model.entity.RoleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTemplateRepository extends JpaRepository<RoleTemplate,Integer> {
}
