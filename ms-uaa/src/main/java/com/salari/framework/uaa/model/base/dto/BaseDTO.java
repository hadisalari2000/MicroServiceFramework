package com.salari.framework.uaa.model.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO<T> {

    private T data;
    private MetaDTO metaDTO;

    public BaseDTO(MetaDTO metaDTO) {
        this.metaDTO = metaDTO;
    }
}
