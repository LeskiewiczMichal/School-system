package com.leskiewicz.schoolsystem.mapper;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    UserMapperImpl userMapper;

    @Test
    public void convertToDtoCorrect() {
        Faculty faculty = Faculty.builder()
                .name("Engineering")
                .build();
        Degree degree = Degree.builder()
                .title(DegreeTitle.BACHELOR)
                .fieldOfStudy("Computer science")
                .faculty(faculty)
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Tester")
                .email("test@example.com")
                .faculty(faculty)
                .degree(degree)
                .password("12345")
                .role(Role.ROLE_STUDENT)
                .build();

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .degree(degree.getFieldOfStudy())
                .faculty(faculty.getName())
                .build();

        UserDto userDto = userMapper.convertToDto(user);

        Assertions.assertEquals(expectedUserDto, userDto);
    }
}
