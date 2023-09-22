package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserMapper {

  UserDto convertToDto(User user);
  Page<UserDto> mapPageToDto(Page<User> users);
}
