package com.salari.framework.uaa.model.domain.user;
import lombok.*;
import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @Column(nullable = false)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String oldPassword;

    @Column(length = 50, nullable = false)
    private String newPassword;

    @Column(length = 50, nullable = false)
    private String confirmPassword;
}
