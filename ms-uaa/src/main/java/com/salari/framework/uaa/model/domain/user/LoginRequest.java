package com.salari.framework.uaa.model.domain.user;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min=3,max = 20)
    @Pattern(regexp = "^[0-9a-zA-Z\\\\-_.@]{3,20}$")
    private String username;

    @NotBlank
    private String password;
}
