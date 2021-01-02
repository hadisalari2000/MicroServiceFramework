package com.salari.framework.uaa.model.entity;

import com.salari.framework.uaa.model.entity.base.BaseEntity;
import com.salari.framework.uaa.model.enums.HttpMethods;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="api")
@Where(clause = "deleted=false")
public class Api extends BaseEntity<Integer> {
    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String url;

    @Size(max = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    private HttpMethods method;

    @Builder.Default
    private Boolean publicAccess = false;

    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "api")
    private Set<TemplatePermission> templatePermissions;

}
