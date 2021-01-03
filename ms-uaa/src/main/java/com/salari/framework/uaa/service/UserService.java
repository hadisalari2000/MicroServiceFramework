package com.salari.framework.uaa.service;

import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.domain.user.LoginRequest;
import com.salari.framework.uaa.model.domain.user.UserRegisterRequest;
import com.salari.framework.uaa.model.domain.user.UserVerificationRequest;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.dto.user.JwtUserDTO;
import com.salari.framework.uaa.model.dto.user.LoginDTO;
import com.salari.framework.uaa.model.dto.user.UserVerificationDTO;
import com.salari.framework.uaa.model.entity.*;
import com.salari.framework.uaa.model.enums.TokenTypes;
import com.salari.framework.uaa.model.mapper.UserMapper;
import com.salari.framework.uaa.model.mapper.UserVerificationMapper;
import com.salari.framework.uaa.repository.*;
import com.salari.framework.uaa.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${application.ms-uaa.login.lock.time-hour}")
    private Long loginLockTime;

    @Value("${application.ms-uaa.login.try.count.max}")
    private Integer loginMaxTryCount;

    @Value("${application.ms-uaa.register.validation.code.expiration-time-minute}")
    private Long registerValidationCodeExpirationTime;

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final UserMapper userMapper;
    private final UserVerificationMapper userVerificationMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public UserService(UserRepository userRepository, RolePermissionRepository rolePermissionRepository, PersonRepository personRepository, RoleRepository roleRepository, UserVerificationRepository userVerificationRepository, UserMapper userMapper, UserVerificationMapper userVerificationMapper, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.userVerificationRepository = userVerificationRepository;
        this.userMapper = userMapper;
        this.userVerificationMapper = userVerificationMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public BaseDTO login(LoginRequest request) {

        User user = getExistUser(request.getUsername());

        if (!user.getActive()) throw ServiceException.getInstance("disabled_user", HttpStatus.UNAUTHORIZED);

        if (user.getLastLoginTryDate() != null && user.getLoginFailedTryCount() > loginMaxTryCount && user.getLastLoginTryDate() + loginLockTime * 3600000L > System.currentTimeMillis())
            throw ServiceException.getInstance("locked_credentials", HttpStatus.LOCKED);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            if (user.getLastLoginTryDate() != null && user.getLastLoginTryDate() + loginLockTime * 3600000L <= System.currentTimeMillis())
                user.setLoginFailedTryCount((short) 1);
            else user.setLoginFailedTryCount((short) (user.getLoginFailedTryCount() + 1));
            user.setLastLoginTryDate(System.currentTimeMillis());
            userRepository.save(user);
            throw ServiceException.getInstance("invalid_credentials", HttpStatus.UNAUTHORIZED);
        } else {
            user.setLoginFailedTryCount((short) 0);
            user.setLastLoginTryDate(null);
            userRepository.save(user);
        }

        Role userLastRole = getLastUserRole(user);
        user.setCurrentRoleId(userLastRole.getId());
        userRepository.save(user);

        LoginDTO loginDTO = LoginDTO.builder()
                .accessToken(tokenProvider.generateJwtToken(user.getId(), userLastRole.getId(), TokenTypes.TOKEN))
                .refreshToken(tokenProvider.generateJwtToken(user.getId(), userLastRole.getId(), TokenTypes.REFRESH))
                .build();

        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(loginDTO)
                .build();
    }

    public BaseDTO refreshToken() {
        User currentUser = getCurrentUser(TokenTypes.REFRESH);
        Role currentRole = getCurrentUserRole();
        LoginDTO loginDTO = LoginDTO.builder()
                .accessToken(tokenProvider.generateJwtToken(currentUser.getId(), currentRole.getId(), TokenTypes.TOKEN))
                .refreshToken(tokenProvider.generateJwtToken(currentUser.getId(), currentRole.getId(), TokenTypes.REFRESH))
                .build();
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(loginDTO)
                .build();
    }

    public BaseDTO registerUser(UserRegisterRequest request) {

        Optional<Person> existPerson = personRepository.findByNationalCode(request.getNationalCode());

        if (existPerson.isPresent() && userRepository.existsByPersonId(existPerson.get().getId()))
            throw ServiceException.getInstance("duplicate_user", HttpStatus.CONFLICT);

        Person person=existPerson.orElse(Person.builder().build());
        LoginDTO loginDTO=createUserAndLoginIt(person,request.getNationalCode(),request.getMobileNumber(),request.getBirthDate());

        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(loginDTO)
                .build();
    }

    private LoginDTO createUserAndLoginIt(Person person, String nationalCode, String mobileNumber, Long birthDate) {

        person.setNationalCode(nationalCode);
        person.setMobileNumber(mobileNumber);
        person.setBirthDate(birthDate);
        person.setCreationDate(System.currentTimeMillis());
        person = personRepository.save(person);

        Role newUserRole = roleRepository.findByKey("public")
                .orElseThrow(()->ServiceException.getInstance("role-global-not-defined",HttpStatus.NOT_FOUND));

        User user = User.builder()
                .active(true)
                .lastLoginTryDate(System.currentTimeMillis())
                .loginFailedTryCount((short) 0)
                .password(passwordEncoder.encode(mobileNumber))
                .personId(person.getId())
                .currentRoleId(newUserRole.getId())
                .username(person.getNationalCode()).build();
        user = userRepository.save(user);
        user.setRoles(Collections.singleton(newUserRole));
        userRepository.save(user);

        return LoginDTO.builder()
                .accessToken(tokenProvider.generateJwtToken(user.getId(), newUserRole.getId(), TokenTypes.TOKEN))
                .build();
    }

    public BaseDTO registerUserWithVerification(UserRegisterRequest request) {

        Optional<Person> person = personRepository.findByNationalCode(request.getNationalCode());

        if (person.isPresent() && userRepository.existsByPersonId(person.get().getId()))
            throw ServiceException.getInstance("duplicate_user", HttpStatus.CONFLICT);

        UserVerificationDTO userVerificationDTO = generateVerificationCode(request);

        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(userVerificationDTO)
                .build();
    }

    public BaseDTO registerUserVerification(UserVerificationRequest request) {

        UserVerification userVerification = getUserVerificationExists(request.getKey(),request.getCode());
        Person person = personRepository.findByNationalCode(userVerification.getNationalCode()).orElse(Person.builder().build());

        if (userRepository.existsByPersonId(person.getId()))
            throw ServiceException.getInstance("duplicate_user", HttpStatus.CONFLICT);

        LoginDTO loginDTO=createUserAndLoginIt(
                person,userVerification.getNationalCode(),userVerification.getMobileNumber(),userVerification.getBirthDate());

        userVerificationRepository.deleteById(userVerification.getId());

        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(loginDTO)
                .build();
    }

    public BaseDTO get(Integer id) {
        User user = getExistUser(id);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(userMapper.USER_DTO(user))
                .build();
    }

    public List<Api> getPermissionsByRoleId(Integer roleId) {
        Optional<List<RolePermission>> rolePermissions = rolePermissionRepository.findAllByRoleId(roleId);
        return rolePermissions
                .map(permissions -> permissions.stream().map(RolePermission::getApi).collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    private Role getLastUserRole(User user) {
        if (user.getCurrentRoleId() != null) {
            Optional<Role> role = roleRepository.findById(user.getCurrentRoleId());
            if (role.isPresent()) return role.get();
        }
        Optional<List<Role>> userRoles = roleRepository.findAllByUsers_Id(user.getId());
        if (userRoles.isPresent())
            return userRoles.get().stream().findFirst().get();
        else
            throw ServiceException.getInstance("role-not-found", HttpStatus.NOT_FOUND);
    }

    private User getExistUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ServiceException.getInstance("user-not-found", HttpStatus.NOT_FOUND));
    }

    private User getExistUser(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> ServiceException.getInstance("invalid_credentials", HttpStatus.UNAUTHORIZED));
    }

    private User getCurrentUser(TokenTypes tokenTypes) {
        JwtUserDTO userToken = tokenProvider.parseToken();
        if (tokenTypes != null && !userToken.getType().equals(tokenTypes))
            throw ServiceException.getInstance("unauthenticated_token", HttpStatus.UNAUTHORIZED);
        Integer userId = userToken.getId();
        return userRepository.findById(userId)
                .orElseThrow(() -> ServiceException.getInstance("unauthenticated_user", HttpStatus.UNAUTHORIZED));
    }

    private Role getCurrentUserRole() {
        JwtUserDTO userToken = tokenProvider.parseToken();
        Integer roleId = userToken.getRoleId();
        Integer userId = userToken.getId();
        Role roleItem = roleRepository.findById(roleId).orElseThrow(() -> ServiceException.getInstance("unauthenticated_role", HttpStatus.UNAUTHORIZED));
        if (getUserRoles(userId).stream().anyMatch(role -> role.getId().equals(roleId))) return roleItem;
        else throw ServiceException.getInstance("unauthenticated_role", HttpStatus.UNAUTHORIZED);
    }

    private List<Role> getUserRoles(Integer userId) {
        return roleRepository.findAllByUsers_Id(userId).orElseGet(ArrayList::new);
    }

    private UserVerificationDTO generateVerificationCode(UserRegisterRequest user)
    {
        UserVerification userVerification = userVerificationRepository.findByNationalCode(user.getNationalCode())
                .orElse(UserVerification.builder().build());

        userVerification.setNationalCode(user.getNationalCode());
        userVerification.setMobileNumber(user.getMobileNumber());
        userVerification.setBirthDate(user.getBirthDate());

        if (userVerification.getCode() == null || System.currentTimeMillis() > userVerification.getExpirationDate()) {
            userVerification.setKey(UUID.randomUUID());
            userVerification.setCode("12345");
            //TODO: uncomment after run sms server
            //userVerification.setCode(PasswordGenerator.generateRandomPinCode(5));
            userVerification.setExpirationDate(System.currentTimeMillis()+(registerValidationCodeExpirationTime*60000L));
        }
        userVerification = userVerificationRepository.save(userVerification);
        return userVerificationMapper.USER_VERIFICATION_DTO(userVerification);
    }

    private UserVerification getUserVerificationExists(UUID key, String code) {
        return userVerificationRepository
                .findByKeyAndCodeAndExpirationDateGreaterThan(key, code, System.currentTimeMillis())
                .orElseThrow(() -> ServiceException.getInstance("not_found_user_verification_code", HttpStatus.BAD_REQUEST));
    }
}
