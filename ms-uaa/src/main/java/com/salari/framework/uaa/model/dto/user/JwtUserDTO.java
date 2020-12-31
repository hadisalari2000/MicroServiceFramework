/* Simple placeholder for info extracted from the JWT * * @author pascal alma */
package com.salari.framework.uaa.model.dto.user;
import com.salari.framework.uaa.model.enums.TokenTypes;
import lombok.*;

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
}
