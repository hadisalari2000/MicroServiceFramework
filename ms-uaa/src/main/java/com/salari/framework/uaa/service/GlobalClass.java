package com.salari.framework.uaa.service;

import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.dto.user.JwtUserDTO;
import com.salari.framework.uaa.model.entity.Api;
import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.HttpMethods;
import com.salari.framework.uaa.model.enums.TokenTypes;
import com.salari.framework.uaa.repository.ApiRepository;
import com.salari.framework.uaa.repository.RoleRepository;
import com.salari.framework.uaa.repository.UserRepository;
import com.salari.framework.uaa.security.JwtTokenProvider;
import lombok.Synchronized;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.util.List;
import java.util.Optional;

@Service
public class GlobalClass {

    public static RoleRepository roleRepository;
    public static ApiRepository apiRepository;
    public static UserRepository userRepository;
    private static JwtTokenProvider jwtTokenProvider;

    public GlobalClass(RoleRepository roleRepository,ApiRepository apiRepository,UserRepository userRepository,JwtTokenProvider jwtTokenProvider) {
        GlobalClass.roleRepository=roleRepository;
        GlobalClass.apiRepository=apiRepository;
        GlobalClass.userRepository=userRepository;
        GlobalClass.jwtTokenProvider=jwtTokenProvider;
    }

    @Synchronized
    public static Optional<List<Role>> getUserRoles(Integer userId) {
        return roleRepository.findAllByUsers_Id(userId);
    }

    @Synchronized
    public static Optional<Api> getApiByUrlAndMethod(String url, HttpMethods httpMethods) {
        return apiRepository.findApiByUrlAndMethod(url,httpMethods);
    }

    @Synchronized
    public static User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->ServiceException.getInstance("user-not-found",HttpStatus.NOT_FOUND));
    }

    @Synchronized
    public static User getCurrentUser(TokenTypes tokenTypes) {
        JwtUserDTO userToken = jwtTokenProvider.parseToken();
        if (tokenTypes != null && !userToken.getType().equals(tokenTypes))
            throw ServiceException.getInstance("unauthenticated_token", HttpStatus.UNAUTHORIZED);
        Integer userId = userToken.getId();

        return getUserById(userId);

    }
}
