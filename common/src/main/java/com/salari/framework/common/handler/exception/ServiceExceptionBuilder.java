package com.salari.framework.common.handler.exception;

import com.salari.framework.common.utility.ApplicationProperties;
import lombok.Synchronized;
import org.springframework.http.HttpStatus;

public class ServiceExceptionBuilder {
    @Synchronized
    public static ServiceException serviceExceptionBuilder(String key, HttpStatus httpStatus) {
        return ServiceException.builder()
                .key(key)
                .message(ApplicationProperties.getProperty(key))
                .httpStatus(httpStatus)
                .build();
    }
}
