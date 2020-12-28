package com.salari.framework.uaa.model.entity;

import com.salari.framework.uaa.model.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends BaseEntity<Integer> {

    @Column(length = 20,nullable = false,unique = true)
    private String username;

    @Column(length = 50,nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean active=true;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="person_id",referencedColumnName = "id")
    private Person person;

    @Column(nullable = false)
    private Integer currentRoleId;

    @Column(nullable = true)
    private Long lastLoginTryDate;

    @Column(nullable = false)
    private Short loginFailedTryCount=0;

    @ManyToMany
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles;
}
