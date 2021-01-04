package com.salari.framework.uaa.model.domain.user;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditRequest {

    @NotNull
    private Integer id;

    @NotBlank
    @Size(min= 3,max=20)
    private String username;

    @NotNull
    private Integer personId;

    @NotNull
    private List<Integer> roleIds;
}
