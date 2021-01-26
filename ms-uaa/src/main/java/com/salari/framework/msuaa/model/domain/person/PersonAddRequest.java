package com.salari.framework.msuaa.model.domain.person;
import com.salari.framework.msuaa.model.enums.Genders;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonAddRequest {

    @NotBlank
    @Size(min=10, max = 10)
    @Pattern(regexp = "[0-9]\\d{9}")
    private String nationalCode;

    @NotBlank
    @Size(min=3, max = 20)
    private String firstName;

    @NotBlank
    @Size(min=3, max = 20)
    private String lastName;

    @NotBlank
    @Size(min=3, max = 20)
    private String fatherName;

    @Enumerated(EnumType.STRING)
    private Genders gender;

    @NotBlank
    @Size(min=11,max=11)
    @Pattern(regexp = "^09\\d{9}$")
    private String mobileNumber;

    @NotNull
    private Long birthDate;
}
