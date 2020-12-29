package com.salari.framework.uaa.model.mapper;
import com.salari.framework.uaa.model.dto.user.UserDTO;
import com.salari.framework.uaa.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO USER_DTO(User user);
}
