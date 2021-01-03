package com.salari.framework.uaa.model.mapper;
import com.salari.framework.uaa.model.dto.user.UserVerificationDTO;
import com.salari.framework.uaa.model.entity.UserVerification;
import org.mapstruct.Mapper;

@Mapper
public interface UserVerificationMapper {
    UserVerificationDTO USER_VERIFICATION_DTO(UserVerification userVerification);
}
