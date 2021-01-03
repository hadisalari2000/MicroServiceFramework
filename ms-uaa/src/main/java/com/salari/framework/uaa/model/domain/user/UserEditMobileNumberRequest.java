package com.salari.framework.uaa.model.domain.user;

import lombok.*;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEditMobileNumberRequest {
    @NotBlank
    @Size(min=10,max=10)
    @Pattern(regexp = "^09\\d{9}$")
    private String mobileNumber;
}
