package com.salari.framework.uaa.handler.exception;

import com.salari.framework.uaa.model.enums.ErrorTypes;
import com.salari.framework.uaa.utility.ApplicationProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class EntityGlobalException extends RuntimeException{

    public EntityGlobalException() {
        super();
    }

    private EntityGlobalException(String message) {
        super(message);
    }

    public static EntityGlobalException getInstance(String key, String... args){
        String message= String.format(ApplicationProperties.getProperty(key), (Object) args);
        return new EntityGlobalException(message);
    }

    public static EntityGlobalException getDuplicateErrorInstance(Class clazz, String... paramsMap){
        String entity=clazz.getSimpleName();
        Map<String, String> searchParams=toMap(String.class, String.class, (Object) paramsMap);
        String message=StringUtils.capitalize(entity) +
                " was not allow duplicate value for parameters " +
                searchParams;
        return new EntityGlobalException(message);
    }

    public static EntityGlobalException getAuthenticateErrorInstance(Class clazz, String... paramsMap){
        String entity=clazz.getSimpleName();
        Map<String, String> searchParams=toMap(String.class, String.class, (Object) paramsMap);
        String message=StringUtils.capitalize(entity) +
                " was not allow duplicate value for parameters " +
                searchParams;
        return new EntityGlobalException(message);
    }

    public static EntityGlobalException getAuthorizeErrorInstance(Class clazz, String... paramsMap){
        String entity=clazz.getSimpleName();
        Map<String, String> searchParams=toMap(String.class, String.class, (Object) paramsMap);
        String message=StringUtils.capitalize(entity) +
                " was not allow duplicate value for parameters " +
                searchParams;
        return new EntityGlobalException(message);
    }

    private static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }
}
