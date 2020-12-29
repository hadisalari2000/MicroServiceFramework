package com.salari.framework.uaa.model.domain.user;
import lombok.*;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangeActivationRequest {

    @NotNull
    private Integer id;

    @NotNull
    private Boolean active;
}
