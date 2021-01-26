package com.salari.framework.msuaa.model.dto.base;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO<T> {
    private T data;
    @Builder.Default
    private HttpStatus status=HttpStatus.OK;
}
