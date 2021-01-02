package com.salari.framework.uaa.model.entity;

import com.salari.framework.uaa.model.entity.base.SimpleBaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="rolesTemplates")
public class RolesTemplates extends SimpleBaseEntity<Integer> {

    @Column(name="role_id" ,nullable = false)
    private Integer roleId;

    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @Column(name="template_id")
    private Integer templateId;

    @ManyToOne()
    @JoinColumn(name="template_id",nullable = false)
    private Template template;
}
