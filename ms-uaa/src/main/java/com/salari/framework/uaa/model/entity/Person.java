package com.salari.framework.uaa.model.entity;

import com.salari.framework.uaa.model.base.entity.BaseEntity;
import com.salari.framework.uaa.model.enums.Genders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="persons")
public class Person extends BaseEntity<Integer> {

    @Column(length = 10,nullable = false,unique = true)
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

    @OneToOne(mappedBy = "person")
    private User user;
}
