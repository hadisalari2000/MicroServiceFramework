package com.salari.framework.msuaa.model.entity;

import com.salari.framework.msuaa.model.entity.base.SimpleBaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import java.util.List;

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

    @ToString.Exclude
    @OneToMany(mappedBy = "template")
    private List<RoleTemplate> roleTemplates;

    @ToString.Exclude
    @OneToMany(mappedBy = "template")
    private List<TemplatePermission> templatePermissions;
}
