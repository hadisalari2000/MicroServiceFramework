package com.salari.framework.msuaa.model.entity;

import com.salari.framework.msuaa.model.entity.base.SimpleBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="role_permission")
public class RolePermission extends SimpleBaseEntity<Integer> {

    @Column(name="api_id",nullable = false)
    private Integer apiId;

    @ManyToOne()
    @JoinColumn(name = "api_id", insertable = false, updatable = false)
    private Api api;

    @Column(name="role_id",nullable = false)
    private Integer roleId;

    @ManyToOne()
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;
}
