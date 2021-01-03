package com.salari.framework.uaa.model.domain.user;

import lombok.*;
import javax.validation.constraints.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVerificationRequest {

    @NotNull
    private UUID key;

    @NotBlank
    @Size(min = 5, max = 5)
    @Pattern(regexp = "^([0-9]*)$")
    private String code;

    private String captcha;

}
