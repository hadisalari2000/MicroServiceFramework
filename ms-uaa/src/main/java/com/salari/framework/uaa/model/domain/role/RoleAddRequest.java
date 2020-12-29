package com.salari.framework.uaa.model.domain.role;
import com.salari.framework.uaa.model.enums.RoleTypes;
import lombok.*;
import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleAddRequest {

    @Column(length = 50,nullable = false)
    private String title;

    @Column(length = 20,nullable = false)
    private String key;

    @Column(length = 100)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private RoleTypes roleType;
}
