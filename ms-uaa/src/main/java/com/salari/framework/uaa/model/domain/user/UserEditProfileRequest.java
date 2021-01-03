package com.salari.framework.uaa.model.domain.user;

import com.salari.framework.uaa.model.enums.Genders;
import lombok.*;
import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEditProfileRequest {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Size(max = 30)
    private String fatherName;

    @NotNull
    private Genders gender;

    @NotBlank
    @Size(max = 40)
    private String email;
}