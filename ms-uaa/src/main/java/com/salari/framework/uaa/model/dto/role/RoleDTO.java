package com.salari.framework.uaa.model.dto.role;
import com.salari.framework.uaa.model.dto.user.UserDTO;
import com.salari.framework.uaa.model.enums.RoleTypes;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String title;
    private String key;
    private String description;
    private Boolean active;
    private RoleTypes roleType;
    private Set<UserDTO> users;
}
