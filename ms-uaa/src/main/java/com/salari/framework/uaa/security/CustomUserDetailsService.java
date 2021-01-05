package com.salari.framework.uaa.security;

import com.salari.framework.uaa.handler.exception.GlobalException;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.repository.UserRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(()->
                GlobalException.getNotFoundErrorInstance(User.class,"username",username));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserDetailsById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(()->
                GlobalException.getNotFoundErrorInstance(User.class,"user-id",id.toString() ));
        return UserPrincipal.create(user);
    }

    @Transactional
    public User loadUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(()->
                GlobalException.getNotFoundErrorInstance(User.class,"user-id",id.toString() ));
    }
}