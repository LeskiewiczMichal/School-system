package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto convertToDto(User user) {
        UserDto.UserDtoBuilder userDto = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail());

        if (user.getFaculty() != null) {
            userDto.faculty(user.getFaculty().getName());
        }
        if (user.getDegree() != null) {
            userDto.degree(user.getDegree().getFieldOfStudy());
        }

        return userDto.build();
    }

}
