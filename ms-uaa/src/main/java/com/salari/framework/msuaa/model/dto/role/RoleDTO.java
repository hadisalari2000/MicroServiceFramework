package com.salari.framework.msuaa.model.dto.role;
import com.salari.framework.msuaa.model.dto.user.UserDTO;
import com.salari.framework.msuaa.model.enums.RoleTypes;
import lombok.*;

import java.util.List;

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
    private List<UserDTO> users;
}
