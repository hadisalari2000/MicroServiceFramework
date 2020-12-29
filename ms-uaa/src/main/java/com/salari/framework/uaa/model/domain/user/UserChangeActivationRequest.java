package com.salari.framework.uaa.model.domain.user;
import lombok.*;
import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangeActivationRequest {

    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Boolean active;
}
