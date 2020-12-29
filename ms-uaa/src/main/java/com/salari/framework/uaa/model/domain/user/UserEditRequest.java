package com.salari.framework.uaa.model.domain.user;
import com.salari.framework.uaa.model.entity.Role;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditRequest {

    @Column(nullable = false)
    private Integer id;

    @Column(length = 20,nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private Integer personId;

    @NonNull
    private Set<Integer> roleIds;
}
