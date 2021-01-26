package com.salari.framework.msuaa.model.dto.user;
import com.salari.framework.msuaa.model.dto.person.PersonDTO;
import com.salari.framework.msuaa.model.dto.role.RoleDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private Boolean active;
    private PersonDTO person;
    private Integer currentRoleId;
    private Long lastLoginTryDate;
    private Short loginFailedTryCount;
    private List<RoleDTO> roles;
}
