package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;

public interface UserMapper {

  UserDto convertToDto(User user);
}
