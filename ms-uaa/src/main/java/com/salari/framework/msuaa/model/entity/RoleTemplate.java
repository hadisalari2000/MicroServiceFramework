package com.salari.framework.msuaa.model.entity;

import com.salari.framework.msuaa.model.entity.base.SimpleBaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="role_Template")
public class RoleTemplate extends SimpleBaseEntity<Integer> {

    @Column(name="role_id" ,nullable = false)
    private Integer roleId;

    @ManyToOne
    @JoinColumn(name="role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name="template_id")
    private Integer templateId;

    @ManyToOne()
    @JoinColumn(name="template_id", insertable = false, updatable = false)
    private Template template;
}
