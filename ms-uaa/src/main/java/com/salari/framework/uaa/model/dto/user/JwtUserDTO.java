/* Simple placeholder for info extracted from the JWT * * @author pascal alma */
package com.salari.framework.uaa.model.dto.user;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.enums.TokenTypes;
import lombok.*;
import java.util.Set;

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
    private Set<Role> roles;
}
