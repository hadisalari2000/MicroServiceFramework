package com.salari.framework.uaa.controller;

import com.salari.framework.uaa.model.dto.base.BaseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    public ResponseEntity<BaseDTO> login(){return null;}


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
