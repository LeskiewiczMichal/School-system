package com.leskiewicz.schoolsystem.mapper;

import static org.mockito.BDDMockito.given;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import org.mockito.Mockito;

public class UserMapperTest {

  // Variables
  Faculty faculty;
  User user;
  Degree degree;

  @BeforeEach
  public void setUp() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);
    user = TestHelper.createUser(faculty, degree);

    given(faculty.getName()).willReturn("Engineering");
    given(degree.getFieldOfStudy()).willReturn("Computer science");
  }

  @Test
  public void convertToDtoCorrectForStudent() {
    UserDto expectedUserDto = UserDto.builder().id(1L).firstName(user.getFirstName())
        .lastName(user.getLastName()).email(user.getEmail()).degree(degree.getFieldOfStudy())
        .faculty(faculty.getName()).build();

    UserDto userDto = UserMapper.convertToDto(user);

    Assertions.assertEquals(expectedUserDto, userDto);
  }

  @Test
  public void convertToDtoCorrectWithoutDegree() {
    User testUser = user.toBuilder().degree(null).role(Role.ROLE_TEACHER).build();

    UserDto expectedUserDto = UserDto.builder().id(1L).firstName(user.getFirstName())
        .lastName(user.getLastName()).email(user.getEmail()).faculty(faculty.getName()).build();

    UserDto userDto = UserMapper.convertToDto(testUser);

    Assertions.assertEquals(expectedUserDto, userDto);
  }

  @Test
  public void convertToDtoThrowsConstraintViolationExceptionOnStudentWithoutDegree() {
    User testUser = user.toBuilder().role(Role.ROLE_STUDENT).degree(null).build();

    Assertions.assertThrows(ConstraintViolationException.class,
        () -> UserMapper.convertToDto(testUser));
  }

  @Test
  public void convertToDtoThrowsIllegalArgumentExceptionOnNoId() {
    User testUser = TestHelper.createUser(faculty, degree).toBuilder().id(null).build();

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> UserMapper.convertToDto(testUser));
  }

  @ParameterizedTest
  @MethodSource("throwsConstraintViolationExceptionOnInvalidUserObjectProvider")
  public void throwsConstraintViolationExceptionOnInvalidUserObject(User user) {
    Assertions.assertThrows(ConstraintViolationException.class,
        () -> UserMapper.convertToDto(user));
  }

  static Stream<Arguments> throwsConstraintViolationExceptionOnInvalidUserObjectProvider() {
    Faculty faculty = Faculty.builder().name("Engineering").build();
    Degree degree = Degree.builder().title(DegreeTitle.BACHELOR).fieldOfStudy("Computer science")
        .faculty(faculty).build();

    User basicUser = User.builder().id(1L).firstName("Test").lastName("Tester")
        .email("test@example.com").faculty(faculty).degree(degree).password("12345")
        .role(Role.ROLE_STUDENT).build();

    return Stream.of(Arguments.of(basicUser.toBuilder().firstName(null).build()),
        Arguments.of(basicUser.toBuilder().lastName(null).build()),
        Arguments.of(basicUser.toBuilder().email(null).build()),
        Arguments.of(basicUser.toBuilder().faculty(null).build()),
        Arguments.of(basicUser.toBuilder().role(null).build()),
        Arguments.of(basicUser.toBuilder().degree(null).build()));
  }
}
