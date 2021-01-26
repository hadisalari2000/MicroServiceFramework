package com.salari.framework.msuaa.model.domain.user;
import lombok.*;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRequest {

    @NotBlank
    @Size(min= 3,max=20)
    private String username;

    @NotBlank
    @Size(max=50)
    private String password;

    @NotNull
    private Integer personId;

    @NotNull
    private Integer roleId;

}
