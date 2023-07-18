package com.leskiewicz.schoolsystem.mapper;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapperImpl;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class UserMapperTest {

    // Inject validator
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final UserMapperImpl userMapper = new UserMapperImpl(validator);

    // Variables
    Faculty faculty;
    User user;
    Degree degree;
    @BeforeEach
    public void setUp() {
        faculty = Faculty.builder()
                .name("Engineering")
                .build();
        degree = Degree.builder()
                .title(DegreeTitle.BACHELOR)
                .fieldOfStudy("Computer science")
                .faculty(faculty)
                .build();

        user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Tester")
                .email("test@example.com")
                .faculty(faculty)
                .degree(degree)
                .password("12345")
                .role(Role.ROLE_STUDENT)
                .build();
    }

    @Test
    public void convertToDtoCorrectForStudent() {


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

    @Test
    public void convertToDtoCorrectWithoutDegree() {
        User testUser = user.toBuilder()
                .degree(null)
                .role(Role.ROLE_TEACHER)
                .build();

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .faculty(faculty.getName())
                .build();

        UserDto userDto = userMapper.convertToDto(testUser);

        Assertions.assertEquals(expectedUserDto, userDto);
    }

    @Test
    public void convertToDtoThrowsIllegalArgumentExceptionOnStudentWithoutDegree() {
        User testUser = user.toBuilder()
                .role(Role.ROLE_STUDENT)
                .degree(null)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                userMapper.convertToDto(testUser));
    }

    @ParameterizedTest
    @MethodSource("throwsIllegalArgumentExceptionOnInvalidUserObjectProvider")
    public void throwsIllegalArgumentExceptionOnInvalidUserObject(User user) {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                userMapper.convertToDto(user));
    }

    static Stream<Arguments> throwsIllegalArgumentExceptionOnInvalidUserObjectProvider() {
        Faculty faculty = Faculty.builder()
                .name("Engineering")
                .build();
        Degree degree = Degree.builder()
                .title(DegreeTitle.BACHELOR)
                .fieldOfStudy("Computer science")
                .faculty(faculty)
                .build();

        User basicUser = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Tester")
                .email("test@example.com")
                .faculty(faculty)
                .degree(degree)
                .password("12345")
                .role(Role.ROLE_STUDENT)
                .build();

        return Stream.of(
                Arguments.of(basicUser.toBuilder().id(null).build()),
                Arguments.of(basicUser.toBuilder().firstName(null).build()),
                Arguments.of(basicUser.toBuilder().lastName(null).build()),
                Arguments.of(basicUser.toBuilder().email(null).build()),
                Arguments.of(basicUser.toBuilder().faculty(null).build()),
                Arguments.of(basicUser.toBuilder().role(null).build()),
                Arguments.of(basicUser.toBuilder().degree(null).build())
        );
    }
}
