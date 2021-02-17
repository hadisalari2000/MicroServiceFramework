package com.salari.framework.common.handler.exception;

import com.salari.framework.common.utility.ApplicationProperties;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.http.HttpStatus.*;

public class GlobalException extends RuntimeException{

    private String message;
    private HttpStatus status;

    public GlobalException() {
        super();
    }

    private GlobalException(String message,HttpStatus status) {
        super(message);
        this.status=status;
    }

    public static GlobalException getInstance(String key, String... args){
        String message= String.format(ApplicationProperties.getProperty(key), (Object) args);
        return new GlobalException(message,BAD_REQUEST);
    }

}