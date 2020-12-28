package com.salari.framework.uaa.model.entity;

import com.salari.framework.uaa.model.base.entity.BaseEntity;
import com.salari.framework.uaa.model.enums.RoleTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role extends BaseEntity<Integer> {

    @Column(length = 50,nullable = false)
    private String title;

    @Column(length = 20,nullable = false,unique = true)
    private String key;

    @Column(length = 100)
    private String description;

    @Column(nullable = false)
    private Boolean active=true;

    @Column(nullable = false)
    private RoleTypes roleType;

    @ToString.Exclude
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
