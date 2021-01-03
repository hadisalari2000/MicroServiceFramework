package com.salari.framework.uaa.model.entity;
import com.salari.framework.uaa.model.entity.base.BaseEntity;
import com.salari.framework.uaa.model.enums.Genders;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="persons")
@Where(clause = "deleted=false")
public class Person extends BaseEntity<Integer> {

    @Column(length = 10,nullable = false,unique = true)
    private String nationalCode;

    @Column(length = 20)
    private String firstName;

    @Column(length = 20)
    private String lastName;

    @Column(length = 20)
    private String fatherName;

    @Enumerated(EnumType.STRING)
    private Genders gender;

    @Column(length = 11,nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private Long birthDate;

    @Column(length = 40)
    private String email;
}
