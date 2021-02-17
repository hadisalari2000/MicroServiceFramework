package com.salari.framework.msuaa.service;
import com.salari.framework.common.handler.exception.NotFoundException;
import com.salari.framework.common.model.base.BaseDTO;
import com.salari.framework.msuaa.model.entity.Api;
import com.salari.framework.msuaa.model.mapper.ApiMapper;
import com.salari.framework.msuaa.repository.ApiRepository;
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
        return BaseDTO.builder().data(apiMapper.API_DTO(api)).build();
    }

    private Api getExistApi(Integer apiId){
        return apiRepository.findById(apiId).orElseThrow(()->
                NotFoundException.getInstance(Api.class, "id", apiId.toString()));
    }
}
