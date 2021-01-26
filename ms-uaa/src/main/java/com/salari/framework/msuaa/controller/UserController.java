package com.salari.framework.msuaa.controller;

import com.salari.framework.msuaa.model.domain.user.*;
import com.salari.framework.msuaa.model.dto.base.BaseDTO;
import com.salari.framework.msuaa.model.enums.Genders;
import com.salari.framework.msuaa.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/apis")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/user/login")
    public ResponseEntity<BaseDTO> login(
            @ApiParam(value = "username",name = "username") @RequestParam String username,
            @ApiParam(value = "password",name = "password") @RequestParam String password){
        LoginRequest request=LoginRequest.builder()
                .username(username.trim())
                .password(password.trim())
                .build();
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @GetMapping("/v1/user/login/refresh")
    public ResponseEntity<BaseDTO> refreshToken(){
        return new ResponseEntity<>(userService.refreshToken(), HttpStatus.OK);
    }

    @PostMapping("/v1/user/register")
    public ResponseEntity<BaseDTO> register(@Valid @RequestBody UserRegisterRequest request){
        return new ResponseEntity<>(userService.registerUser(request),HttpStatus.OK);
    }

    @PostMapping("/v1/user/registerWithVerification")
    public ResponseEntity<BaseDTO> registerWithVerification(@Valid @RequestBody UserRegisterRequest request){
        return new ResponseEntity<>(userService.registerUserWithVerification(request),HttpStatus.OK);
    }

    @PutMapping("/v1/user/verification")
    public ResponseEntity<BaseDTO> verification(@Valid @RequestBody UserVerificationRequest userVerificationBean) {
        return new ResponseEntity<>(userService.userVerification(userVerificationBean), HttpStatus.OK);
    }

    @PostMapping("/v1/user/password/forget")
    public ResponseEntity<BaseDTO> forgetPassword(@Valid @RequestBody PasswordForgetRequest passwordForgetRequest) {
        return new ResponseEntity<>(userService.forgetPassword(passwordForgetRequest), HttpStatus.OK);
    }

    @PutMapping("/v1/user/password/verification")
    public ResponseEntity<BaseDTO> forgetPasswordVerification(@Valid @RequestBody UserVerificationRequest userVerificationRequest) {
        return new ResponseEntity<>(userService.forgetPasswordVerification(userVerificationRequest), HttpStatus.OK);
    }

    @PutMapping("/v1/user/password/reset")
    public ResponseEntity<BaseDTO> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        return new ResponseEntity<>(userService.resetPassword(passwordResetRequest), HttpStatus.OK);
    }

    @PutMapping("/v1/user/password")
    public ResponseEntity<BaseDTO> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        return new ResponseEntity<>(userService.changePassword(passwordChangeRequest), HttpStatus.OK);
    }

    @GetMapping("/v1/user/profile")
    public ResponseEntity<BaseDTO> getUserProfile() {
        return new ResponseEntity<>(userService.getUserProfile(), HttpStatus.OK);
    }

    @PutMapping("/v1/user/profile")
    public ResponseEntity<BaseDTO> editProfile(@Valid @RequestBody UserEditProfileRequest userEditProfileRequest) {
        return new ResponseEntity<>(userService.editUserProfile(userEditProfileRequest), HttpStatus.OK);
    }

    @PutMapping("/v1/user/mobileNumber")
    public ResponseEntity<BaseDTO> changeMobileNumber(@Valid @RequestBody UserEditMobileNumberRequest userEditMobileNumberRequest) {
        return new ResponseEntity<>(userService.changeMobileNumber(userEditMobileNumberRequest), HttpStatus.OK);
    }

    @GetMapping("/v1/user/changeRole")
    public ResponseEntity<BaseDTO> changeRole(@ApiParam(value = "roleId", name = "roleId") @RequestParam Integer roleId) {
        return new ResponseEntity<>(userService.changeRole(roleId), HttpStatus.OK);
    }

    @GetMapping("/v1/user")
    public ResponseEntity<BaseDTO> getUserById(@ApiParam(value = "id", name = "id") @RequestParam Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/v1/users/status")
    public ResponseEntity<BaseDTO> changeUsersStatus(@Valid @RequestBody UserChangeActivationRequest changeActivationRequest) {
        return new ResponseEntity<>(userService.changeUserStatus(changeActivationRequest), HttpStatus.OK);
    }

    @DeleteMapping("/v1/user")
    public ResponseEntity<BaseDTO> deleteById(@Valid @ApiParam(value="id",name = "id") @RequestParam Integer id){
        return new ResponseEntity<>(userService.deleteUser(id),HttpStatus.OK);
    }

    @GetMapping("/v1/users/filter")
    public ResponseEntity<BaseDTO> getByFilterForAdmin(@ApiParam(value = "firstName", name = "firstName") @RequestParam(required = false) String firstName,
                                                       @ApiParam(value = "lastName", name = "lastName") @RequestParam(required = false) String lastName,
                                                       @ApiParam(value = "username", name = "username") @RequestParam(required = false) String username,
                                                       @ApiParam(value = "nationalCode", name = "nationalCode") @RequestParam(required = false) String nationalCode,
                                                       @ApiParam(value = "mobileNumber", name = "mobileNumber") @RequestParam(required = false) String mobileNumber,
                                                       @ApiParam(value = "gender", name = "gender") @RequestParam(required = false) Genders gender,
                                                       @ApiParam(value = "email", name = "email") @RequestParam(required = false) String email,
                                                       @ApiParam(value = "roleId", name = "roleId") @RequestParam(required = false) Integer roleId,
                                                       @ApiParam(value = "page", name = "page") @RequestParam Integer page) {
        UserFilterRequest request = UserFilterRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .mobileNumber(mobileNumber)
                .nationalCode(nationalCode)
                .username(username)
                .gender(gender)
                .email(email)
                .roleId(roleId)
                .build();
        return new ResponseEntity<>(userService.getUsersByFilter(request,page), HttpStatus.OK);
    }
}
