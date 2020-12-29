package com.salari.framework.uaa.model.dto.base;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaMapDTO {

    private String key;
    private String message;
}
