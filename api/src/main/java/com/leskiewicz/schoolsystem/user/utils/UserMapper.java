package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.User;

public interface UserMapper {

    UserDto convertToDto(User user);
}
