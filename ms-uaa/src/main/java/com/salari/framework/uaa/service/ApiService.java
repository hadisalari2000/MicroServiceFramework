package com.salari.framework.uaa.service;
import com.salari.framework.uaa.handler.exception.EntityNotFoundException;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.entity.Api;
import com.salari.framework.uaa.model.mapper.ApiMapper;
import com.salari.framework.uaa.repository.ApiRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final ApiRepository apiRepository;
    private final ApiMapper apiMapper;

    public ApiService(ApiRepository apiRepository, ApiMapper apiMapper) {
        this.apiRepository = apiRepository;
        this.apiMapper = apiMapper;
    }


    public BaseDTO getApi(Integer id){
        Api api=getExistApi(id);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(apiMapper.API_DTO(api))
                .build();
    }

    private Api getExistApi(Integer apiId){
        return apiRepository.findById(apiId)
                .orElseThrow(()->EntityNotFoundException.getInstance(Api.class, "id", apiId.toString()));
    }
}
