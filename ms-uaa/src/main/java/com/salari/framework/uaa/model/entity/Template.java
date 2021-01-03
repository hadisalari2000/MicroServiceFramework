package com.salari.framework.uaa.model.entity;

import com.salari.framework.uaa.model.entity.base.SimpleBaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="Templates")
public class Template extends SimpleBaseEntity<Integer> {

    @Column(length = 50,nullable = false,unique = true)
    private String title;

    @OneToMany(mappedBy = "template")
    private Set<RoleTemplate> roleTemplates;

    @OneToMany(mappedBy = "template")
    private Set<TemplatePermission> templatePermissions;
}
