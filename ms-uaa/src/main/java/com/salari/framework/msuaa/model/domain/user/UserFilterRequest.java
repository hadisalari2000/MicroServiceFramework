package com.salari.framework.msuaa.model.domain.user;

import com.salari.framework.msuaa.model.enums.Genders;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFilterRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String mobileNumber;
    private String email;
    private Genders gender;
    private Integer roleId;
}
