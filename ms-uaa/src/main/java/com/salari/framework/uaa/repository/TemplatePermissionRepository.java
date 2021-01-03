package com.salari.framework.uaa.repository;

import com.salari.framework.uaa.model.entity.TemplatePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplatePermissionRepository extends JpaRepository<TemplatePermission,Integer> {

}
