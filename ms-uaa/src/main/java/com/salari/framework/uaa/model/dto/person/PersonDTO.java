package com.salari.framework.uaa.model.dto.person;
import com.salari.framework.uaa.model.dto.user.UserDTO;
import com.salari.framework.uaa.model.enums.Genders;
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
