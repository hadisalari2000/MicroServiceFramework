package com.salari.framework.uaa.model.domain.person;
import com.salari.framework.uaa.model.enums.Genders;
import lombok.*;
import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonAddRequest {

    @Column(length = 10,nullable = false)
    private String nationalCode;

    @Column(length = 20,nullable = false)
    private String firstName;

    @Column(length = 20,nullable = false)
    private String lastName;

    @Column(length = 20,nullable = false)
    private String fatherName;

    @Enumerated(EnumType.STRING)
    private Genders gender;

    @Column(length = 11,nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private Long birthDate;
}
