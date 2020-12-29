package com.salari.framework.uaa.model.domain.user;
import lombok.*;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotNull
    private Integer id;

    @NotBlank
    @Size(max=50)
    private String oldPassword;

    @NotBlank
    @Size(max=50)
    private String newPassword;

    @NotBlank
    @Size(max=50)
    private String confirmPassword;
}
