package com.salari.framework.uaa.model.domain.user;
import lombok.*;
import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRequest {

    @Column(length = 20,nullable = false,unique = true)
    private String username;

    @Column(length = 50,nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer personId;

    @Column(nullable = false)
    private Integer roleId;

}
