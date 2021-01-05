package com.salari.framework.uaa.service;
import com.salari.framework.uaa.handler.exception.EntityGlobalException;
import com.salari.framework.uaa.handler.exception.EntityNotFoundException;
import com.salari.framework.uaa.model.domain.person.PersonAddRequest;
import com.salari.framework.uaa.model.domain.person.PersonEditRequest;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.entity.Person;
import com.salari.framework.uaa.model.mapper.PersonMapper;
import com.salari.framework.uaa.repository.PersonRepository;
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
        checkDuplicatePerson(request.getNationalCode(),request.getMobileNumber(),null);
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
        checkDuplicatePerson(request.getNationalCode(),request.getMobileNumber(),person);
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
                .orElseThrow(()-> EntityNotFoundException.getInstance(Person.class,"id",personId.toString()));
    }

    private void checkDuplicatePerson(String nationalCode,String mobileNumber,Person person){

        Optional<Person> existPerson=personRepository.findByNationalCodeOrMobileNumber(nationalCode,mobileNumber);

        if(existPerson.isPresent() && !(person!=null && existPerson.get().getId().equals(person.getId())) && existPerson.get().getNationalCode().equals(nationalCode))
            throw  EntityGlobalException.getDuplicateErrorInstance(Person.class, "national-code",nationalCode);

        if(existPerson.isPresent() && !(person!=null && existPerson.get().getId().equals(person.getId())) && existPerson.get().getMobileNumber().equals(mobileNumber))
            throw EntityGlobalException.getDuplicateErrorInstance(Person.class, "mobile-number",mobileNumber);
    }

}
