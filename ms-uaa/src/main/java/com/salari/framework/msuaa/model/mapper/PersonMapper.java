package com.salari.framework.msuaa.model.mapper;
import com.salari.framework.msuaa.model.dto.person.PersonDTO;
import com.salari.framework.msuaa.model.entity.Person;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {
    PersonDTO PERSON_DTO(Person person);
}
