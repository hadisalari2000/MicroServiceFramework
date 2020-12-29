package com.salari.framework.uaa.model.domain.role;
import com.salari.framework.uaa.model.enums.RoleTypes;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleAddRequest {

    @NotBlank
    @Size(min=3, max = 50)
    private String title;

    @NotBlank
    @Size(min=3, max = 20)
    private String key;

    @Size(max= 100)
    private String description;

    @NotNull
    private RoleTypes roleType;
}
