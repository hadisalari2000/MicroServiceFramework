package com.salari.framework.uaa.model.domain.user;

import lombok.*;
import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordForgetRequest {

    @NotBlank
    @Size(min = 10,max=10)
    @Pattern(regexp = "[0-9]\\d{9}")
    private String nationalCode;
}
