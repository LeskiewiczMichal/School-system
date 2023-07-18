package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final Validator validator;

    @Override
    public UserDto convertToDto(User user) {
        // Perform manual validation
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Invalid User object: " + violations.toString());
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("Invalid User object: id missing");
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .faculty(user.getFaculty().getName());

        if (user.getRole() == Role.ROLE_STUDENT) {
            userDto.degree(user.getDegree().getFieldOfStudy());
        } else {
            userDto.degree(null);
        }

        return userDto.build();
    }

}
