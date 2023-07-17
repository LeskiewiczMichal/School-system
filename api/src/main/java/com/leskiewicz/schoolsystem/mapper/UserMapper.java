package com.leskiewicz.schoolsystem.mapper;

import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.model.User;

public interface UserMapper {

    UserDto convertToDto(User user);
}
