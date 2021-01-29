package com.salari.framework.msuaa.utility;

import com.salari.framework.msuaa.handler.exception.GlobalException;
import com.salari.framework.msuaa.handler.exception.NotFoundException;
import com.salari.framework.msuaa.model.dto.base.BaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RestTemplateUtility {

    private final RestTemplate restTemplate;

    @Autowired
    private RestTemplateUtility(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BaseDTO sendRequest(BaseDTO baseDTO) {
        return baseDTO;
    }

    public <T> T sendRequest(String url, HttpMethod httpMethod, Object body, ParameterizedTypeReference<T> typeReference, String channel) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        httpHeaders.setAll(Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(name -> name, request::getHeader)));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (channel !=null) httpHeaders.set("Channel",channel);
        HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, typeReference);
        return responseEntity.getBody();
    }

    public <T> T sendRequest(String url, HttpMethod httpMethod, Object body,ParameterizedTypeReference<T> typeReference) {
        return sendRequest(url,httpMethod,body,typeReference,null);
    }

    public BaseDTO sendRequest(String url, HttpMethod httpMethod, Object body) {
        return sendRequest(url,httpMethod,body,new ParameterizedTypeReference<BaseDTO>() {},null);
    }

    public BaseDTO sendRequest(String url,HttpMethod httpMethod, Object body,MediaType mediaType) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(name -> name, request::getHeader)));
        httpHeaders.setContentType(mediaType);
        HttpEntity<?> httpEntity = new HttpEntity<>(mapBody(body), httpHeaders);
        ResponseEntity<BaseDTO> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, BaseDTO.class);
        return responseEntity.getBody();
    }

    private MultiValueMap<String, Object> mapBody(Object obj) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.getType().equals(MultipartFile[].class))
                    Arrays.stream((MultipartFile[])field.get(obj)).forEach(file-> map.add(field.getName(), multipartFileToResource(file)));
                else if (field.getType().equals(MultipartFile.class)) map.add(field.getName(), multipartFileToResource(field.get(obj)));
                else map.add(field.getName(), field.get(obj));
            }
            catch (Exception e) {
                throw GlobalException.getInstance("invalid_input");
            }
        }
        return map;
    }

    private ByteArrayResource multipartFileToResource(Object file) {
        if (file == null)
            throw NotFoundException.getInstance(ByteArrayResource.class,"file", "multipartFileToResource");
        try {
            return new ByteArrayResource(((MultipartFile)file).getBytes()) {
                @Override
                public String getFilename() {
                    return ((MultipartFile)file).getOriginalFilename();
                }
            };
        }
        catch (Exception e) {
            throw GlobalException.getInstance("invalid_file_input");
        }
    }
}