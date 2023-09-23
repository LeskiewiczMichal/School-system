package com.leskiewicz.schoolsystem.user;

import static com.leskiewicz.schoolsystem.builders.UserBuilder.anUser;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.builders.UserBuilder;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.utils.UserMapperImpl;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

  User user = anUser().build();

  @InjectMocks private UserMapperImpl userMapper;

  static Stream<Arguments> throwsConstraintViolationExceptionOnInvalidUserObjectProvider() {
    UserBuilder basicUser = anUser();

    return Stream.of(
        Arguments.of(basicUser.firstName(null).build()),
        Arguments.of(basicUser.lastName(null).build()),
        Arguments.of(basicUser.email(null).build()),
        Arguments.of(basicUser.faculty(null).build()),
        Arguments.of(basicUser.role(null).build()),
        Arguments.of(basicUser.degree(null).build()));
  }

  @Test
  public void convertToDtoCorrectForStudent() {
    UserDto expectedUserDto =
        UserDto.builder()
            .id(1L)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .degree(user.getDegree().getFieldOfStudy())
            .faculty(user.getFaculty().getName())
            .facultyId(1L)
            .build();

    UserDto userDto = userMapper.convertToDto(user);

    Assertions.assertEquals(expectedUserDto, userDto);
  }

  @Test
  public void convertToDtoCorrectForTeacher() {
    TeacherDetails teacherDetails = Mockito.mock(TeacherDetails.class);
    User user = anUser().role(Role.ROLE_TEACHER).teacherDetails(teacherDetails).build();
    UserDto expectedUserDto =
        UserDto.builder()
            .id(1L)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .degree(user.getDegree().getFieldOfStudy())
            .faculty(user.getFaculty().getName())
            .facultyId(1L)
            .build();

    UserDto userDto = userMapper.convertToDto(user);

    Assertions.assertEquals(expectedUserDto, userDto);
  }

  @Test
  public void convertToDtoThrowsConstraintViolationExceptionOnStudentWithoutDegree() {
    User testUser = anUser().role(Role.ROLE_STUDENT).degree(null).build();

    Assertions.assertThrows(
        ConstraintViolationException.class, () -> userMapper.convertToDto(testUser));
  }

  @Test
  public void convertToDtoThrowsIllegalArgumentExceptionOnNoId() {
    User testUser = anUser().id(null).build();

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> userMapper.convertToDto(testUser));
  }

  @ParameterizedTest
  @MethodSource("throwsConstraintViolationExceptionOnInvalidUserObjectProvider")
  public void throwsConstraintViolationExceptionOnInvalidUserObject(User user) {
    Assertions.assertThrows(
        ConstraintViolationException.class, () -> userMapper.convertToDto(user));
  }

  @Test
  public void mapPageToDtoMapsUsersProperly() {
    Page<User> usersPage =
        new PageImpl<User>(
            List.of(
                anUser().firstName("Johnny").lastName("Silverhand").build(),
                anUser().firstName("Mark").lastName("Cole").build()));

    Page<UserDto> usersDtoPage = userMapper.mapPageToDto(usersPage);

    Assertions.assertEquals(2, usersDtoPage.getContent().size());
    Assertions.assertEquals("Johnny", usersDtoPage.getContent().get(0).getFirstName());
    Assertions.assertEquals("Silverhand", usersDtoPage.getContent().get(0).getLastName());
    Assertions.assertEquals("Mark", usersDtoPage.getContent().get(1).getFirstName());
    Assertions.assertEquals("Cole", usersDtoPage.getContent().get(1).getLastName());
  }
}
