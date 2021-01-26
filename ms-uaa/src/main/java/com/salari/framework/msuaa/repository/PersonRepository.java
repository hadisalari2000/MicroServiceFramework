package com.salari.framework.msuaa.repository;

import com.salari.framework.msuaa.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person,Integer> {

    Optional<Person> findById(Integer id);
    Optional<Person> findByNationalCode(String nationalCode);
    Optional<Person> findByMobileNumber(String mobileNumber);
    Optional<Person> findByNationalCodeOrMobileNumber(String nationalCode,String mobileNumber);
    List<Person> findAllByIdIsNotNull();
}
