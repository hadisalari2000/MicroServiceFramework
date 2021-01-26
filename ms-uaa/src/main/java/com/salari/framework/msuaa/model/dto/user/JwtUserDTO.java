/* Simple placeholder for info extracted from the JWT * * @author pascal alma */
package com.salari.framework.msuaa.model.dto.user;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salari.framework.msuaa.model.entity.Role;
import com.salari.framework.msuaa.model.enums.TokenTypes;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserDTO {
    private Integer id;
    private Integer roleId;
    private Long expiration;
    private TokenTypes type;
    private String jti;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Role> roles;
}
