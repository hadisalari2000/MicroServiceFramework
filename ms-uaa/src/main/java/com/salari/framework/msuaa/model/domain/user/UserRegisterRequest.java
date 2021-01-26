package com.salari.framework.msuaa.model.domain.user;

import lombok.*;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    @NotBlank
    @Size(min=11,max=11)
    @Pattern(regexp = "^09\\d{9}$")
    private String mobileNumber;

    @NotBlank
    @Size(min = 10,max=10)
    @Pattern(regexp = "[0-9]\\d{9}")
    private String nationalCode;

    @NotNull
    private Long birthDate;
}
