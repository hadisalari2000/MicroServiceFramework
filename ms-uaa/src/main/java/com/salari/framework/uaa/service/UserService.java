package com.salari.framework.uaa.service;
import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.domain.user.LoginRequest;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.dto.user.LoginDTO;
import com.salari.framework.uaa.model.entity.Role;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.TokenTypes;
import com.salari.framework.uaa.model.mapper.UserMapper;
import com.salari.framework.uaa.repository.RoleRepository;
import com.salari.framework.uaa.repository.UserRepository;
import com.salari.framework.uaa.security.JwtTokenProvider;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public BaseDTO get(Integer id) {
        User user = getExistUser(id);
        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(userMapper.USER_DTO(user))
                .build();
    }

    public BaseDTO getByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> ServiceException.getInstance("user-not-found", HttpStatus.NOT_FOUND));

        return BaseDTO.builder()
                .metaDTO(MetaDTO.getInstance())
                .data(userMapper.USER_DTO(user))
                .build();
    }

    private User getExistUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ServiceException.getInstance("user-not-found", HttpStatus.NOT_FOUND));
    }

    public BaseDTO login(LoginRequest request) {

        User user = userRepository.findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow(() -> ServiceException.getInstance("invalid_credentials", HttpStatus.UNAUTHORIZED));

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

        if (StringUtils.isNotBlank(request.getApplicationKey())) {
            List<Permission> allUserPermissions = getUserPermissions(user, null);
            List<Integer> applicationIds = allUserPermissions.stream().
                    filter(p -> p.getPermissionClassType().getKey().equals(PermissionClassTypes.Application))
                    .map(p -> p.getDestinationId().intValue()).collect(Collectors.toList());
            if (applicationIds.isEmpty())
                throw serviceExceptionBuilder("invalid_client", HttpStatus.FORBIDDEN);
            if (getApplicationsExistsByKey(applicationIds, loginRequest.getApplicationKey()).isEmpty())
                throw serviceExceptionBuilder("invalid_client", HttpStatus.FORBIDDEN);
        }

        LoginDTO loginDTO = LoginDTO.builder()
                .accessToken(tokenProvider.generateJwtToken(user.getId(), userLastRole.getId(), TokenTypes.TOKEN))
                .refreshToken(tokenProvider.generateJwtToken(user.getId(), userLastRole.getId(), TokenTypes.REFRESH))
                .build();

        return BaseDTO.builder()
                .meta(MetaDTO.getInstance())
                .data(loginDTO)
                .build();

        return new BaseDTO();
    }

    private Role getLastUserRole(User user) {
        if (user.getCurrentRoleId() != null) {
            Optional<Role> role = roleRepository.findById(user.getCurrentRoleId());
            if (role.isPresent()) return role.get();
        }
        Optional<List<Role>> userRoles = roleRepository.findAllByUsers_Id(user.getId());
        if(userRoles.isPresent())
            return userRoles.get().stream().findFirst().get();
        else
           throw ServiceException.getInstance("role-not-found", HttpStatus.NOT_FOUND);
    }
}
