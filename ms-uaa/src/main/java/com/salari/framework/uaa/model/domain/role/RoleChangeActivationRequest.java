package com.salari.framework.uaa.model.domain.role;
import lombok.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeActivationRequest {
    @NotNull
    private Integer id;

    @NotNull
    private Boolean active;
}
