package com.salari.framework.uaa.repository;

import com.salari.framework.uaa.model.entity.Api;
import com.salari.framework.uaa.model.enums.HttpMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiRepository extends JpaRepository<Api,Integer> {

    Optional<Api> findById(Integer id);
    Optional<Api> findApiByUrlAndMethod(String url, HttpMethods method);
    List<Api> findAllByIdIsNotNull();
}
