package com.salari.framework.uaa.controller;

import com.salari.framework.uaa.model.domain.user.LoginRequest;
import com.salari.framework.uaa.model.domain.user.UserRegisterRequest;
import com.salari.framework.uaa.model.domain.user.UserVerificationRequest;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("v1/user/login")
    public ResponseEntity<BaseDTO> login(
            @ApiParam(value = "username",name = "username") @RequestParam String username,
            @ApiParam(value = "password",name = "password") @RequestParam String password){
        LoginRequest request=LoginRequest.builder()
                .username(username.trim())
                .password(password.trim())
                .build();
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @GetMapping("v1/user/login/refresh")
    public ResponseEntity<BaseDTO> refreshToken(){
        return new ResponseEntity<>(userService.refreshToken(), HttpStatus.OK);
    }

    @PostMapping("v1/user/register")
    public ResponseEntity<BaseDTO> register(@Valid @RequestBody UserRegisterRequest request){
        return new ResponseEntity<>(userService.registerUser(request),HttpStatus.OK);
    }

    @PostMapping("v1/user/registerWithVerification")
    public ResponseEntity<BaseDTO> registerWithVerification(@Valid @RequestBody UserRegisterRequest request){
        return new ResponseEntity<>(userService.registerUserWithVerification(request),HttpStatus.OK);
    }

    @PutMapping("v1/user/verification")
    public ResponseEntity<BaseDTO> verification(@Valid @RequestBody UserVerificationRequest userVerificationBean) {
        return new ResponseEntity<>(userService.registerUserVerification(userVerificationBean), HttpStatus.OK);
    }

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
