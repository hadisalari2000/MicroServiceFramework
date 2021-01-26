package com.salari.framework.msuaa.model.dto.person;
import com.salari.framework.msuaa.model.dto.user.UserDTO;
import com.salari.framework.msuaa.model.enums.Genders;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String fatherName;
    private Genders gender;
    private String mobileNumber;
    private Long birthDate;
    private UserDTO user;
}
