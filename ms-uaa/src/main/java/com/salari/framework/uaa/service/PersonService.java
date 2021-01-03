package com.salari.framework.uaa.service;
import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.domain.person.PersonAddRequest;
import com.salari.framework.uaa.model.domain.person.PersonEditRequest;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.entity.Person;
import com.salari.framework.uaa.model.mapper.PersonMapper;
import com.salari.framework.uaa.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public BaseDTO getPerson(Integer id){
        Person person=getExistPerson(id);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(personMapper.PERSON_DTO(person))
                .build();
    }

    public BaseDTO addPerson(PersonAddRequest request){
        if(personRepository.findByNationalCodeOrMobileNumber(request.getNationalCode(),request.getMobileNumber()).isPresent())
            throw ServiceException.getInstance("person-duplicated", HttpStatus.BAD_REQUEST);

        Person person=Person.builder()
                .birthDate(request.getBirthDate())
                .fatherName(request.getFatherName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobileNumber(request.getMobileNumber())
                .nationalCode(request.getNationalCode())
                .gender(request.getGender())
                .build();

        personRepository.save(person);

        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(personMapper.PERSON_DTO(person))
                .build();
    }

    public BaseDTO editPerson(PersonEditRequest request){
        Person person=getExistPerson(request.getId());

        Optional<Person> existPerson=personRepository.findByNationalCodeOrMobileNumber(request.getNationalCode(),request.getMobileNumber());
        if(existPerson.isPresent() && !existPerson.get().getId().equals(request.getId()))
            throw ServiceException.getInstance("person-duplicated", HttpStatus.BAD_REQUEST);

        person.setBirthDate(request.getBirthDate());
        person.setFatherName(request.getFatherName());
        person.setFirstName(request.getFirstName());
        person.setGender(request.getGender());
        person.setLastName(request.getLastName());
        person.setMobileNumber(request.getMobileNumber());
        person.setNationalCode(request.getNationalCode());
        personRepository.save(person);

        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(personMapper.PERSON_DTO(person))
                .build();
    }

    public BaseDTO deletePerson(Integer id){
        Person person=getExistPerson(id);
        personRepository.delete(person);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(null)
                .build();
    }

    private Person getExistPerson(Integer personId){
        return personRepository.findById(personId)
                .orElseThrow(()->ServiceException.getInstance("person-not-found", HttpStatus.NOT_FOUND));
    }
}
