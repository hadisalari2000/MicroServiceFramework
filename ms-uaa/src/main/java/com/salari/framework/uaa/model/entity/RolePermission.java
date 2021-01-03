package com.salari.framework.uaa.model.entity;

import com.salari.framework.uaa.model.entity.base.SimpleBaseEntity;
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
@Table(name="Templates")
public class RolePermission extends SimpleBaseEntity<Integer> {

    @Column(name="api_id",nullable = false)
    private Integer apiId;

    @ManyToOne()
    @JoinColumn(name = "api_id")
    private Api api;

    @Column(name="role_id",nullable = false)
    private Integer roleId;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;
}
