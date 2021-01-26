package com.salari.framework.msuaa.model.mapper;
import com.salari.framework.msuaa.model.dto.api.ApiDTO;
import com.salari.framework.msuaa.model.entity.Api;
import org.mapstruct.Mapper;

@Mapper
public interface ApiMapper {
    ApiDTO API_DTO(Api api);
}
