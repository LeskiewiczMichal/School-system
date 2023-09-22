package com.leskiewicz.schoolsystem.user;

import static org.mockito.BDDMockito.given;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.anUser;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.utils.UserMapperImpl;
import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

  // Variables
  Faculty faculty;
  User user;
  Degree degree;
  @InjectMocks private UserMapperImpl userMapper;

  static Stream<Arguments> throwsConstraintViolationExceptionOnInvalidUserObjectProvider() {
    Faculty faculty = Faculty.builder().name("Engineering").build();
    Degree degree =
        Degree.builder()
            .title(DegreeTitle.BACHELOR)
            .fieldOfStudy("Computer science")
            .faculty(faculty)
            .build();

    User basicUser =
        User.builder()
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
        Arguments.of(basicUser.toBuilder().firstName(null).build()),
        Arguments.of(basicUser.toBuilder().lastName(null).build()),
        Arguments.of(basicUser.toBuilder().email(null).build()),
        Arguments.of(basicUser.toBuilder().faculty(null).build()),
        Arguments.of(basicUser.toBuilder().role(null).build()),
        Arguments.of(basicUser.toBuilder().degree(null).build()));
  }

  @BeforeEach
  public void setUp() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);
    user = TestHelper.createUser(faculty, degree);
  }

  @Test
  public void convertToDtoCorrectForStudent() {
    // Setup
    given(faculty.getName()).willReturn("FacultyName");
    given(faculty.getId()).willReturn(1L);

    UserDto expectedUserDto =
        UserDto.builder()
            .id(1L)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .degree(degree.getFieldOfStudy())
            .faculty(faculty.getName())
            .facultyId(1L)
            .build();

    // Call function
    UserDto userDto = userMapper.convertToDto(user);

    Assertions.assertEquals(expectedUserDto, userDto);
  }

  @Test
  public void convertToDtoCorrectForTeacher() {
    // Setup
    TeacherDetails teacherDetails = Mockito.mock(TeacherDetails.class);
    User testUser =
        user.toBuilder()
            .degree(null)
            .teacherDetails(teacherDetails)
            .role(Role.ROLE_TEACHER)
            .profilePictureName("test.jpg")
            .build();
    given(faculty.getName()).willReturn("FacultyName");
    given(faculty.getId()).willReturn(1L);
    given(teacherDetails.getId()).willReturn(1L);

    UserDto expectedUserDto =
        UserDto.builder()
            .id(1L)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .faculty(faculty.getName())
            .facultyId(1L)
            .teacherDetailsId(1L)
            .profilePictureName(user.getProfilePictureName())
            .build();

    // Call function
    UserDto userDto = userMapper.convertToDto(testUser);

    // Assertions
    Assertions.assertEquals(expectedUserDto, userDto);
  }

  @Test
  public void convertToDtoThrowsConstraintViolationExceptionOnStudentWithoutDegree() {
    User testUser = user.toBuilder().role(Role.ROLE_STUDENT).degree(null).build();

    Assertions.assertThrows(
        ConstraintViolationException.class, () -> userMapper.convertToDto(testUser));
  }

  @Test
  public void convertToDtoThrowsIllegalArgumentExceptionOnNoId() {
    User testUser = TestHelper.createUser(faculty, degree).toBuilder().id(null).build();

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
