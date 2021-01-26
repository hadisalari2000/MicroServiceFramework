package com.salari.framework.msuaa.repository;

import com.salari.framework.msuaa.model.entity.TemplatePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplatePermissionRepository extends JpaRepository<TemplatePermission,Integer> {

}
