package com.salari.framework.msuaa.model.entity;
import com.salari.framework.msuaa.model.enums.RoleTypes;
import com.salari.framework.msuaa.model.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.List;

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

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RoleTypes roleType;

    @ToString.Exclude
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @ToString.Exclude
    @OneToMany(mappedBy = "role")
    private List<RoleTemplate> rolesTemplates;

    @ToString.Exclude
    @OneToMany(mappedBy = "role")
    private List<RolePermission> rolePermissions;
}
