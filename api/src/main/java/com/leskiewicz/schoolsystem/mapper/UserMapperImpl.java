package com.leskiewicz.schoolsystem.mapper;

import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapperImpl implements UserMapper {

    private ModelMapper modelMapper;

    @Override
    public UserDto convertToDto(User user) {
//        UserDto userDto = modelMapper.map(user, UserDto.class);

        UserDto userDto = new UserDto();

        if (user.getFaculty() != null) {
            userDto.setFaculty(user.getFaculty().getName());
        }
        if (user.getDegree() != null) {
            userDto.setDegree(user.getDegree().getFieldOfStudy());
        }
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLastName(user.getLastName());
        userDto.setFirstName(user.getFirstName());

        return userDto;
    }

}
