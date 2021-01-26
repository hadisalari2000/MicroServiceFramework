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
@Table(name="Template_permission")
public class TemplatePermission extends SimpleBaseEntity<Integer> {

    @Column(name="api_id",nullable = false)
    private Integer apiId;

    @ManyToOne()
    @JoinColumn(name = "api_id", insertable = false, updatable = false)
    private Api api;

    @Column(name="template_id",nullable = false)
    private Integer templateId;

    @ManyToOne()
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    private Template template;
}
