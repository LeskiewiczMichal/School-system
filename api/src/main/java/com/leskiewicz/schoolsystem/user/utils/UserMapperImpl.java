package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

  private final Logger logger = LoggerFactory.getLogger(UserMapperImpl.class);

  @Override
  public UserDto convertToDto(User user) {
    // Perform manual validation
    ValidationUtils.validate(user);
    if (user.getId() == null) {
      throw new IllegalArgumentException(ErrorMessages.objectInvalidPropertyMissing("User", "id"));
    }

    Faculty userFaculty = user.getFaculty();
    logger.debug("User with ID: {} registered into faculty: {}", user.getId(), userFaculty.getName());

    // Create dto from user
    UserDto.UserDtoBuilder userDto =
        UserDto.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .faculty(userFaculty.getName())
            .facultyId(userFaculty.getId());
    if (user.getRole() == Role.ROLE_STUDENT) {
      userDto.degree(user.getDegree().getFieldOfStudy());
    } else {
      userDto.degree(null);
    }
    logger.debug("Converted User entity with ID: {} to UserDto", user.getId());

    return userDto.build();
  }
}
