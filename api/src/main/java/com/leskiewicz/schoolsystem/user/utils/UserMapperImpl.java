package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.utils.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<User, UserDto> {

  private final Logger logger = LoggerFactory.getLogger(UserMapperImpl.class);

  @Override
  public UserDto mapToDto(User user) {
    // Perform manual validation
    ValidationUtils.validate(user);
    if (user.getId() == null) {
      throw new IllegalArgumentException(ErrorMessages.objectInvalidPropertyMissing("User", "id"));
    }

    Faculty userFaculty = user.getFaculty();

    // Create dto from user
    UserDto.UserDtoBuilder userDto =
        UserDto.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .faculty(userFaculty.getName())
            .facultyId(userFaculty.getId())
            .role(user.getRole());

    // If user is not a student, he won't have a degree
    if (user.getRole() == Role.ROLE_STUDENT) {
      userDto.degree(user.getDegree().getFieldOfStudy());
      userDto.degreeId(user.getDegree().getId());
    } else {
      userDto.degree(null);
      userDto.degreeId(null);
    }

    // Optional profile picture
    if (user.getProfilePictureName() != null) {
      userDto.profilePictureName(user.getProfilePictureName());
    }

    // If user is teacher, he will have teacher details associated
    if (user.getRole() == Role.ROLE_TEACHER) {
      userDto.teacherDetailsId(user.getTeacherDetails().getId());
    }

    UserDto mappedUserDto = userDto.build();
    ValidationUtils.validate(mappedUserDto);

    logger.debug("Converted User entity with ID: {} to UserDto", user.getId());
    return mappedUserDto;
  }

  @Override
  public Page<UserDto> mapPageToDto(Page<User> users) {
    return users.map(this::mapToDto);
  }
}
