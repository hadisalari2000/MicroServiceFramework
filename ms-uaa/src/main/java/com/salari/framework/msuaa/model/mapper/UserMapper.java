package com.salari.framework.msuaa.model.mapper;
import com.salari.framework.msuaa.model.dto.user.UserDTO;
import com.salari.framework.msuaa.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO USER_DTO(User user);
}
