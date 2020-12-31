package com.salari.framework.uaa.model.mapper;
import com.salari.framework.uaa.model.dto.api.ApiDTO;
import com.salari.framework.uaa.model.entity.Api;
import org.mapstruct.Mapper;

@Mapper
public interface ApiMapper {
    ApiDTO API_DTO(Api api);
}
