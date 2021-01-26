package com.salari.framework.msuaa.model.entity;

import com.salari.framework.msuaa.model.entity.base.SimpleBaseEntity;
import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_verification")
public class UserVerification  extends SimpleBaseEntity<Integer> {

    @Builder.Default
    @Column(name="key", nullable = false)
    private UUID key = UUID.randomUUID();

    @Column(length = 11, nullable = false)
    private String mobileNumber;

    @Column(length = 11,nullable = false)
    private String nationalCode;

    private Long birthDate;

    @Column(length = 5,nullable = false)
    private String code;

    @Column(nullable = false)
    private Long expirationDate;
}
