package com.salari.framework.uaa.model.mapper;
import com.salari.framework.uaa.model.dto.person.PersonDTO;
import com.salari.framework.uaa.model.entity.Person;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {
    PersonDTO PERSON_DTO(Person person);
}
