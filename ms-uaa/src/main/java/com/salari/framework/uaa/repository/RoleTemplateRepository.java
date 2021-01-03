package com.salari.framework.uaa.repository;

import com.salari.framework.uaa.model.entity.RoleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTemplateRepository extends JpaRepository<RoleTemplate,Integer> {
}
