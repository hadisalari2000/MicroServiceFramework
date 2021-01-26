package com.salari.framework.msuaa.handler.exception;

import com.salari.framework.msuaa.utility.ApplicationProperties;
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

    public static GlobalException getDuplicateErrorInstance(Class clazz, String... paramsMap){
        String entity=clazz.getSimpleName();
        Map<String, String> searchParams=toMap(String.class, String.class, (Object) paramsMap);
        String message=String.format(
                ApplicationProperties.getProperty("duplicated"),
                ApplicationProperties.getProperty(entity.toLowerCase()),
                searchParams);
        return new GlobalException(message,BAD_REQUEST);
    }

    private static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }
}