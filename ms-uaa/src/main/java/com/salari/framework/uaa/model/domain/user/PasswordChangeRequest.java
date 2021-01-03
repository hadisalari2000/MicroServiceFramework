package com.salari.framework.uaa.model.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 45)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d !\"#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~]{6,45}$")
    private String password;

    @NotBlank
    @Size(min = 6, max = 45)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d !\"#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~]{6,45}$")
    private String confirmPassword;

}
