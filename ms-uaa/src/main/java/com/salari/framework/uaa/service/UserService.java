package com.salari.framework.uaa.service;
import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.mapper.UserMapper;
import com.salari.framework.uaa.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public BaseDTO get(Integer id){
        User user=getExistUser(id);
        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(userMapper.USER_DTO(user))
                .build();
    }

    public BaseDTO getByUsername(String username){
        User user=userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()->ServiceException.getInstance("user-not-found",HttpStatus.NOT_FOUND));

        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(userMapper.USER_DTO(user))
                .build();
    }

    private User getExistUser(Integer userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> ServiceException.getInstance("user-not-found", HttpStatus.NOT_FOUND));
    }
}
