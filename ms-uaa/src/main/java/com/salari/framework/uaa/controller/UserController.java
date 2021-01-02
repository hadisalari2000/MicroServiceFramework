package com.salari.framework.uaa.controller;

import com.salari.framework.uaa.model.domain.user.LoginRequest;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("v1/user/login")
    public ResponseEntity<BaseDTO> login(@ApiParam(value = "username",name = "username") @RequestParam String username,
                                         @ApiParam(value = "password",name = "password") @RequestParam String password)
    {
        LoginRequest request=LoginRequest.builder()
                .username(username.trim())
                .password(password.trim())
                .build();
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    public ResponseEntity<BaseDTO> register(){return null;}

    public ResponseEntity<BaseDTO> verification(){return null;}

    public ResponseEntity<BaseDTO> changePassword(){return null;}

    public ResponseEntity<BaseDTO> resetPassword(){return null;}

    public ResponseEntity<BaseDTO> forgetPassword(){return null;}

    public ResponseEntity<BaseDTO> forgetPasswordVerification(){return null;}

    public ResponseEntity<BaseDTO> getUserProfile(){return null;}

    public ResponseEntity<BaseDTO> editUserProfile(){return null;}

    public ResponseEntity<BaseDTO> changeRole(){return null;}

    public ResponseEntity<BaseDTO> getUserById(){return null;}

    public ResponseEntity<BaseDTO> deleteUserById(){return null;}

    public ResponseEntity<BaseDTO> changeUserStatus(){return null;}

    public ResponseEntity<BaseDTO> getAllUsers(){return null;}

    public ResponseEntity<BaseDTO> getByFilter(){return null;}
}
