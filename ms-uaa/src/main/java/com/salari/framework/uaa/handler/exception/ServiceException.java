package com.salari.framework.uaa.handler.exception;
import com.salari.framework.uaa.utility.ApplicationProperties;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {

    private String key;
    private String message;
    private HttpStatus httpStatus;

    @Synchronized
    public static ServiceException getInstance(String key, HttpStatus httpStatus) {
        return ServiceException.builder()
                .httpStatus(httpStatus)
                .key(key)
                .message(ApplicationProperties.getProperty(key))
                .build();
    }
}
