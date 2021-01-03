package com.salari.framework.uaa.model.entity;
import com.salari.framework.uaa.model.entity.base.BaseEntity;
import com.salari.framework.uaa.model.enums.RoleTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="roles")
@Where(clause = "deleted=false")
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

    @OneToMany(mappedBy = "role")
    private Set<RoleTemplate> rolesTemplates;

    @OneToMany(mappedBy = "role")
    private Set<RolePermission> rolePermissions;
}
