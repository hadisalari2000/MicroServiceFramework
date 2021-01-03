package com.salari.framework.uaa.model.dto.user;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVerificationDTO {
    private UUID key;
    private String mobileNumber;
    private String code;
    private Long timeToExpire;
}
