package com.salari.framework.uaa.model.dto.base;
import lombok.*;

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
