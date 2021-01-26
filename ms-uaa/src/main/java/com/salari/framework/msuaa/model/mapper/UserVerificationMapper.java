package com.salari.framework.msuaa.model.mapper;
import com.salari.framework.msuaa.model.dto.user.UserVerificationDTO;
import com.salari.framework.msuaa.model.entity.UserVerification;
import org.mapstruct.Mapper;

@Mapper
public interface UserVerificationMapper {
    UserVerificationDTO USER_VERIFICATION_DTO(UserVerification userVerification);
}
