package com.salari.framework.uaa.model.dto.user;
import com.salari.framework.uaa.model.dto.person.PersonDTO;
import com.salari.framework.uaa.model.dto.role.RoleDTO;
import lombok.*;

import java.util.Set;

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
    private Set<RoleDTO> roles;
}
