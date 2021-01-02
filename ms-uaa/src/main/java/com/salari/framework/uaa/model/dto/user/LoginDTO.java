package com.salari.framework.uaa.model.dto.user;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String accessToken;
    private String refreshToken;
}
