package com.salari.framework.uaa.model.domain.role;
import lombok.*;
import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeActivationRequest {
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Boolean active;
}
