package com.salari.framework.uaa.security;

import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.entity.Api;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.HttpMethods;
import com.salari.framework.uaa.repository.ApiRepository;
import com.salari.framework.uaa.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ApiRepository apiRepository;

    public CustomUserDetailsService(UserRepository userRepository, ApiRepository apiRepository) {
        this.userRepository = userRepository;
        this.apiRepository = apiRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> ServiceException.getInstance("user-not-found", HttpStatus.NOT_FOUND));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserDetailsById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> ServiceException.getInstance("user-not-found", HttpStatus.NOT_FOUND));
        return UserPrincipal.create(user);
    }

    @Transactional
    public User loadUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(()-> ServiceException.getInstance("user-not-found", HttpStatus.NOT_FOUND));
    }

    public Optional<Api> findApiByUrlAndMethod(String path, HttpMethods method) {
        return apiRepository.findApiByUrlAndMethod(path, method);
    }
}