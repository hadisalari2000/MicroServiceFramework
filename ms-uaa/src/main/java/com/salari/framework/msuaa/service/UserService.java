package com.salari.framework.msuaa.service;

import com.salari.framework.msuaa.handler.exception.AuthorizeException;
import com.salari.framework.msuaa.handler.exception.ForbiddenException;
import com.salari.framework.msuaa.handler.exception.GlobalException;
import com.salari.framework.msuaa.handler.exception.NotFoundException;
import com.salari.framework.msuaa.model.domain.user.*;
import com.salari.framework.msuaa.model.dto.base.BaseDTO;
import com.salari.framework.msuaa.model.dto.base.PagerDTO;
import com.salari.framework.msuaa.model.dto.user.JwtUserDTO;
import com.salari.framework.msuaa.model.dto.user.LoginDTO;
import com.salari.framework.msuaa.model.dto.user.UserDTO;
import com.salari.framework.msuaa.model.entity.*;
import com.salari.framework.msuaa.model.mapper.PersonMapper;
import com.salari.framework.msuaa.model.mapper.UserMapper;
import com.salari.framework.msuaa.model.mapper.UserVerificationMapper;
import com.salari.framework.msuaa.model.dto.user.UserVerificationDTO;
import com.salari.framework.msuaa.model.enums.TokenTypes;
import com.salari.framework.msuaa.repository.*;
import com.salari.framework.msuaa.security.JwtTokenProvider;
import com.salari.framework.msuaa.utility.PageableUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PersonMapper personMapper;
    private final UserVerificationMapper userVerificationMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final PageableUtility pageableUtility;

    public UserService(UserRepository userRepository, RolePermissionRepository rolePermissionRepository, PersonRepository personRepository, RoleRepository roleRepository, UserVerificationRepository userVerificationRepository, UserMapper userMapper, PersonMapper personMapper, UserVerificationMapper userVerificationMapper, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, PageableUtility pageableUtility) {
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.userVerificationRepository = userVerificationRepository;
        this.userMapper = userMapper;
        this.personMapper = personMapper;
        this.userVerificationMapper = userVerificationMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.pageableUtility = pageableUtility;
    }

    public BaseDTO login(LoginRequest request) {

        User user = getExistUser(request.getUsername());

        if (!user.getActive())
            throw GlobalException.getInstance("disabled_user",request.getUsername());

        if (user.getLastLoginTryDate() != null && user.getLoginFailedTryCount() > loginMaxTryCount && user.getLastLoginTryDate() + loginLockTime * 3600000L > System.currentTimeMillis())
            throw GlobalException.getInstance("locked_credentials", request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            if (user.getLastLoginTryDate() != null && user.getLastLoginTryDate() + loginLockTime * 3600000L <= System.currentTimeMillis())
                user.setLoginFailedTryCount((short) 1);
            else user.setLoginFailedTryCount((short) (user.getLoginFailedTryCount() + 1));
            user.setLastLoginTryDate(System.currentTimeMillis());
            userRepository.save(user);
            throw GlobalException.getInstance("invalid_credentials");
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

        return BaseDTO.builder().data(loginDTO).build();
    }

    public BaseDTO refreshToken() {
        User currentUser = getCurrentUser(TokenTypes.REFRESH);
        Role currentRole = getCurrentUserRole();
        LoginDTO loginDTO = LoginDTO.builder()
                .accessToken(tokenProvider.generateJwtToken(currentUser.getId(), currentRole.getId(), TokenTypes.TOKEN))
                .refreshToken(tokenProvider.generateJwtToken(currentUser.getId(), currentRole.getId(), TokenTypes.REFRESH))
                .build();
        return BaseDTO.builder().data(loginDTO).build();
    }

    public BaseDTO registerUser(UserRegisterRequest request) {

        Optional<Person> existPerson = personRepository.findByNationalCode(request.getNationalCode());

        if (existPerson.isPresent() && userRepository.existsByPersonId(existPerson.get().getId()))
            throw GlobalException.getDuplicateErrorInstance(User.class,"national-code", request.getNationalCode());

        Person person = existPerson.orElse(Person.builder().build());
        LoginDTO loginDTO = createUserAndLoginIt(person, request.getNationalCode(), request.getMobileNumber(), request.getBirthDate());

        return BaseDTO.builder().data(loginDTO).build();
    }

    @Transactional
    LoginDTO createUserAndLoginIt(Person person, String nationalCode, String mobileNumber, Long birthDate) {

        person.setNationalCode(nationalCode);
        person.setMobileNumber(mobileNumber);
        person.setBirthDate(birthDate);
        person.setCreationDate(System.currentTimeMillis());
        person = personRepository.save(person);

        Role newUserRole = roleRepository.findByKey("public").orElseThrow(() ->
                NotFoundException.getInstance(Role.class,"key", "public"));

        User user = User.builder()
                .active(true)
                .lastLoginTryDate(System.currentTimeMillis())
                .loginFailedTryCount((short) 0)
                .password(passwordEncoder.encode(mobileNumber))
                .personId(person.getId())
                .currentRoleId(newUserRole.getId())
                .username(person.getNationalCode())
                .build();
        user = userRepository.save(user);
        user.setRoles(Collections.singletonList(newUserRole));
        userRepository.save(user);

        return LoginDTO.builder()
                .accessToken(tokenProvider.generateJwtToken(user.getId(), newUserRole.getId(), TokenTypes.TOKEN))
                .build();
    }

    public BaseDTO registerUserWithVerification(UserRegisterRequest request) {

        Optional<Person> person = personRepository.findByNationalCode(request.getNationalCode());

        if (person.isPresent() && userRepository.existsByPersonId(person.get().getId()))
            throw GlobalException.getDuplicateErrorInstance(User.class,"national-code",request.getNationalCode());

        UserVerificationDTO userVerificationDTO = generateVerificationCode(request.getNationalCode(), request.getMobileNumber(), request.getBirthDate());

        return BaseDTO.builder().data(userVerificationDTO).build();
    }

    public BaseDTO userVerification(UserVerificationRequest request) {

        UserVerification userVerification = getUserVerificationExists(request.getKey(), request.getCode());
        Person person = personRepository.findByNationalCode(userVerification.getNationalCode()).orElse(Person.builder().build());

        if (userRepository.existsByPersonId(person.getId()))
            throw GlobalException.getDuplicateErrorInstance(User.class,"national-code",userVerification.getNationalCode());

        LoginDTO loginDTO = createUserAndLoginIt(
                person, userVerification.getNationalCode(), userVerification.getMobileNumber(), userVerification.getBirthDate());

        userVerificationRepository.deleteById(userVerification.getId());

        return BaseDTO.builder().data(loginDTO).build();
    }

    public BaseDTO forgetPassword(PasswordForgetRequest request) {
        User user = getUserByNationalCode(request.getNationalCode());
        Person person = user.getPerson();
        UserVerificationDTO userVerificationDTO = generateVerificationCode(person.getNationalCode(), person.getMobileNumber(), person.getBirthDate());
        return BaseDTO.builder().data(userVerificationDTO).build();
    }

    public BaseDTO forgetPasswordVerification(UserVerificationRequest request) {
        UserVerification userVerification = getUserVerificationExists(request.getKey(), request.getCode());
        User user = getUserByNationalCode(userVerification.getNationalCode());
        LoginDTO loginDTO = LoginDTO.builder()
                .accessToken(tokenProvider.generateJwtToken(user.getId(), user.getCurrentRoleId(), TokenTypes.TOKEN))
                .build();
        userVerificationRepository.deleteById(userVerification.getId());
        return BaseDTO.builder().data(loginDTO).build();
    }

    public BaseDTO resetPassword(PasswordResetRequest request) {
        User user = getCurrentUser(TokenTypes.TOKEN);
        passwordMatcher(request.getPassword(), request.getConfirmPassword());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return BaseDTO.builder().data(userMapper.USER_DTO(user)).build();
    }

    public BaseDTO changePassword(PasswordChangeRequest request) {
        User user = getCurrentUser(TokenTypes.TOKEN);

        passwordMatcher(request.getPassword(), request.getConfirmPassword());

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw GlobalException.getInstance("invalid_old_password");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return BaseDTO.builder().data(null).build();
    }

    public BaseDTO getUserProfile() {
        return BaseDTO.builder().data(personMapper.PERSON_DTO(getCurrentUser().getPerson())).build();
    }

    public BaseDTO editUserProfile(UserEditProfileRequest request) {
        User user = getCurrentUser();
        Person person = user.getPerson();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setFatherName(request.getFatherName());
        person.setGender(request.getGender());
        person.setEmail(request.getEmail());
        person = personRepository.save(person);

        return BaseDTO.builder().data(personMapper.PERSON_DTO(person)).build();
    }

    public BaseDTO changeMobileNumber(UserEditMobileNumberRequest request) {
        User user = getCurrentUser();
        user.getPerson().setMobileNumber(request.getMobileNumber());
        userRepository.save(user);
        UserDTO userDTO = userMapper.USER_DTO(user);
        userDTO.setPerson(personMapper.PERSON_DTO(user.getPerson()));
        return BaseDTO.builder().data(userDTO).build();
    }

    public BaseDTO changeRole(Integer roleId) {
        User currentUser = getCurrentUser();
        if (getUserRoles(currentUser.getId()).stream().noneMatch(role -> role.getId().equals(roleId)))
            throw GlobalException.getInstance("invalid_role");

        currentUser.setCurrentRoleId(roleId);
        userRepository.save(currentUser);

        LoginDTO loginDTO = LoginDTO.builder()
                .accessToken(tokenProvider.generateJwtToken(currentUser.getId(), roleId, TokenTypes.TOKEN))
                .refreshToken(tokenProvider.generateJwtToken(currentUser.getId(), roleId, TokenTypes.REFRESH))
                .build();

        return BaseDTO.builder().data(loginDTO).build();
    }

    public BaseDTO getUserById(Integer id) {
        User user = getExistUser(id);
        return BaseDTO.builder().data(userMapper.USER_DTO(user)).build();
    }

    public BaseDTO changeUserStatus(UserChangeActivationRequest request) {

        User user = userRepository.findById(request.getId()).orElseThrow(() ->
                NotFoundException.getInstance(User.class,"id",request.getId().toString()));

        user.setActive(request.getActive());
        userRepository.save(user);

        return BaseDTO.builder().data(userMapper.USER_DTO(user)).build();
    }

    public BaseDTO deleteUser(Integer id) {
        User user = getExistUser(id);
        Person person = user.getPerson();

        person.setDeleted(true);
        personRepository.save(person);

        user.setDeleted(true);
        user.setRoles(null);
        userRepository.save(user);

        return BaseDTO.builder().data(null).build();
    }

    public BaseDTO getUsersByFilter(UserFilterRequest request, Integer page) {
        Page<User> users = userRepository.findUsersByFilters(request, pageableUtility.createPageable(page));
        PagerDTO<UserDTO> pagerDTO = new PagerDTO<>(users.map(userMapper::USER_DTO));
        return BaseDTO.builder().data(pagerDTO).build();
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
            throw NotFoundException.getInstance(Role.class,"user-id",user.getId().toString());
    }

    private User getExistUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                NotFoundException.getInstance(User.class, "user-id",userId.toString()));
    }

    private User getExistUser(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> GlobalException.getInstance("invalid_credentials"));
    }

    private User getCurrentUser() {
        return getCurrentUser(null);
    }

    private User getCurrentUser(TokenTypes tokenTypes) {
        JwtUserDTO userToken = tokenProvider.parseToken();
        if (tokenTypes != null && !userToken.getType().equals(tokenTypes))
            throw AuthorizeException.getInstance("unauthenticated_token");
        Integer userId = userToken.getId();
        return userRepository.findById(userId).orElseThrow(() ->
                ForbiddenException.getInstance(User.class));
    }

    private Role getCurrentUserRole() {
        JwtUserDTO userToken = tokenProvider.parseToken();
        Integer roleId = userToken.getRoleId();
        Integer userId = userToken.getId();
        Role roleItem = roleRepository.findById(roleId).orElseThrow(() ->
                ForbiddenException.getInstance(Role.class));
        if (getUserRoles(userId).stream().anyMatch(role -> role.getId().equals(roleId))) return roleItem;
        else throw ForbiddenException.getInstance(Role.class);
    }

    private List<Role> getUserRoles(Integer userId) {
        return roleRepository.findAllByUsers_Id(userId).orElseGet(ArrayList::new);
    }

    private UserVerificationDTO generateVerificationCode(String nationalCode, String mobileNumber, Long birthDate) {
        UserVerification userVerification = userVerificationRepository.findByNationalCode(nationalCode)
                .orElse(UserVerification.builder().build());

        userVerification.setNationalCode(nationalCode);
        userVerification.setMobileNumber(mobileNumber);
        userVerification.setBirthDate(birthDate);

        if (userVerification.getCode() == null || System.currentTimeMillis() > userVerification.getExpirationDate()) {
            userVerification.setKey(UUID.randomUUID());
            userVerification.setCode("12345");
            //TODO: uncomment after run sms server
            //userVerification.setCode(PasswordGenerator.generateRandomPinCode(5));
            userVerification.setExpirationDate(System.currentTimeMillis() + (registerValidationCodeExpirationTime * 60000L));
        }
        userVerification = userVerificationRepository.save(userVerification);
        return userVerificationMapper.USER_VERIFICATION_DTO(userVerification);
    }

    private UserVerification getUserVerificationExists(UUID key, String code) {
        return userVerificationRepository
                .findByKeyAndCodeAndExpirationDateGreaterThan(key, code, System.currentTimeMillis())
                .orElseThrow(() -> GlobalException.getInstance("not_found_user_verification_code"));
    }

    private User getUserByNationalCode(String nationalCode) {
        Person person = personRepository.findByNationalCode(nationalCode).orElseThrow(() ->
                NotFoundException.getInstance(Person.class,"national-code",nationalCode));
        return userRepository.findByPersonId(person.getId()).orElseThrow(() ->
                NotFoundException.getInstance(Person.class,"id", person.getId().toString()));
    }

    private void passwordMatcher(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw GlobalException.getInstance("not_match_password");
        }
    }
}
