package com.leskiewicz.schoolsystem.user;

import static com.leskiewicz.schoolsystem.builders.UserBuilder.anUser;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.userDtoFrom;
import static com.leskiewicz.schoolsystem.builders.CourseBuilder.aCourse;
import static com.leskiewicz.schoolsystem.builders.CourseBuilder.courseDtoFrom;
import static com.leskiewicz.schoolsystem.builders.TeacherDetailsBuilder.aTeacherDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.builders.UserBuilder;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsRepository;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

  // Repositories
  @Mock private UserRepository userRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private TeacherDetailsRepository teacherDetailsRepository;

  // Mappers
  @Mock private UserMapper userMapper;
  @Mock private CourseMapper courseMapper;

  @InjectMocks private UserServiceImpl userService;

  List<User> usersList =
      List.of(
          anUser().build(),
          anUser()
              .firstName("Johnny")
              .lastName("Silverhand")
              .email("johnny@example.com")
              .password("qwerty")
              .profilePictureName("Mypicture")
              .build());
  List<UserDto> userDtosList =
      List.of(userDtoFrom(usersList.get(0)), userDtoFrom(usersList.get(1)));

  @Test
  public void getUsersReturnsPagedUserDtos() {
    when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(usersList));
    when(userMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(userDtosList));

    Page<UserDto> result = userService.getUsers(PageRequest.of(0, 1));

    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(userDtosList.get(0), result.getContent().get(0));
    Assertions.assertEquals(userDtosList.get(1), result.getContent().get(1));
  }

  @Test
  public void getUserCoursesReturnsPagedCourseDtosWhenUserIsStudent() {
    List<Course> coursesList = List.of(aCourse().build());
    CourseDto courseDto = courseDtoFrom(aCourse().build());
    when(userRepository.findById(any(Long.class)))
        .thenReturn(Optional.ofNullable(anUser().build()));
    when(courseRepository.findCoursesByUserId(any(Long.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(coursesList));
    when(courseMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(List.of(courseDto)));

    Page<CourseDto> result = userService.getUserCourses(1L, PageRequest.of(0, 1));

    assertCourseList(courseDto, result);
  }

  @Test
  public void getUserCoursesReturnsPagedCourseDtosWhenUserIsTeacher() {
    List<Course> coursesList = List.of(aCourse().build());
    CourseDto courseDto = courseDtoFrom(aCourse().build());
    when(userRepository.findById(any(Long.class)))
        .thenReturn(Optional.ofNullable(anUser().role(Role.ROLE_TEACHER).build()));
    when(courseMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(List.of(courseDto)));
    when(courseRepository.findCoursesByTeacherId(any(Long.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(coursesList));

    Page<CourseDto> result = userService.getUserCourses(1L, PageRequest.of(0, 1));

    assertCourseList(courseDto, result);
  }

  @Test
  public void getUserCoursesReturns404IfUserDoesntExist() {
    given(userRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> userService.getUserCourses(1L, null));
  }

  private void assertCourseList(CourseDto courseDto, Page<CourseDto> result) {
    Assertions.assertEquals(1, result.getTotalElements());
    Assertions.assertEquals(courseDto, result.getContent().get(0));
  }

  @Test
  public void searchReturnsPagedUserDtos() {
    String LAST_NAME = userDtosList.get(0).getLastName();
    String FIRST_NAME = userDtosList.get(0).getFirstName();
    Role ROLE = userDtosList.get(0).getRole();
    when(userRepository.searchUsersByLastNameAndFirstNameAndRole(
            LAST_NAME, FIRST_NAME, ROLE, PageRequest.of(0, 2)))
        .thenReturn(new PageImpl<>(usersList));
    when(userMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(userDtosList));

    Page<UserDto> result = userService.search(LAST_NAME, FIRST_NAME, ROLE, PageRequest.of(0, 2));

    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(2, result.getNumberOfElements());
    Assertions.assertEquals(0, result.getNumber());
    Assertions.assertEquals(2, result.getSize());
    Assertions.assertEquals(userDtosList.get(0), result.getContent().get(0));
    Assertions.assertEquals(userDtosList.get(1), result.getContent().get(1));
  }

  @Test
  public void getTeacherDetailsReturnsCorrectTeacherDetails() {
    TeacherDetails teacherDetails = setUpGetTeacherDetailsTest();
    TeacherDetails testTeacherDetails = userService.getTeacherDetails(1L);

    Assertions.assertEquals(teacherDetails, testTeacherDetails);
    verify(userRepository).existsById(any(Long.class));
  }

  @Test
  public void getTeacherDetailsCallsExistsById() {
    setUpGetTeacherDetailsTest();
    userService.getTeacherDetails(1L);

    verify(userRepository).existsById(any(Long.class));
  }

  private TeacherDetails setUpGetTeacherDetailsTest() {
    TeacherDetails teacherDetails = aTeacherDetails().build();
    given(userRepository.existsById(any(Long.class))).willReturn(true);
    given(teacherDetailsRepository.findByUserId(any(Long.class)))
        .willReturn(Optional.of(teacherDetails));

    return teacherDetails;
  }

  @Test
  public void getTeacherDetailsThrowsEntityNotFound() {
    given(userRepository.existsById(any(Long.class))).willReturn(false);
    Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getTeacherDetails(1L));
  }
}
