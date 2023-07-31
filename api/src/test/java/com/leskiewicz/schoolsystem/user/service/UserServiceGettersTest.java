package com.leskiewicz.schoolsystem.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsRepository;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class UserServiceGettersTest {

  // Repositories
  @Mock private UserRepository userRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private TeacherDetailsRepository teacherDetailsRepository;

  // Mappers
  @Mock private UserMapper userMapper;
  @Mock private CourseMapper courseMapper;

  @InjectMocks private UserServiceImpl userService;

  Faculty faculty;
  Degree degree;
  User user;

  @BeforeEach
  public void setUp() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);
    user = TestHelper.createUser(faculty, degree);
  }

  @Test
  public void getUsersReturnsPagedUsers() {
    // Set up test data
    Faculty faculty = Mockito.mock(Faculty.class);
    Degree degree = Mockito.mock(Degree.class);
    List<User> userList =
        Arrays.asList(
            TestHelper.createUser(faculty, degree), TestHelper.createUser(faculty, degree));
    List<UserDto> userDtos =
        Arrays.asList(
            TestHelper.createUserDto("FacultyTest", "DegreeTest"),
            TestHelper.createUserDto("TestFaculty", "TestDegree"));

    CommonTests.serviceGetAll(
        User.class,
        userList,
        userDtos,
        userRepository::findAll,
        userMapper::convertToDto,
        userService::getUsers);
  }

  @Test
  public void getUserCoursesReturnsPagedCourses() {
    // Set up test data
    Faculty faculty = TestHelper.createFaculty();
    User teacher = TestHelper.createTeacher(faculty);
    List<Course> courseList = TestHelper.createCoursesList(faculty, teacher);
    List<CourseDto> courseDtos =
        Arrays.asList(
            TestHelper.createCourseDto(courseList.get(0)),
            TestHelper.createCourseDto(courseList.get(1)));

    CommonTests.serviceGetAllResourcesRelated(
        Course.class,
        courseList,
        courseDtos,
        courseRepository::findCoursesByUserId,
        courseMapper::convertToDto,
        userRepository::existsById,
        userService::getUserCourses);
  }

  @Test
  public void getUserCoursesReturns404IfUserDoesntExist() {
    // Set up test data
    Pageable pageable = Mockito.mock(PageRequest.class);

    // Mock repository
    given(userRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> userService.getUserCourses(1L, pageable));
  }

  @Test
  public void getTeacherDetailsReturnsTeacherDetails() {
    // Set up test data
    Faculty faculty = TestHelper.createFaculty();
    User teacher = TestHelper.createTeacher(faculty);
    TeacherDetails teacherDetails = TestHelper.createTeacherDetails(teacher);
    given(userRepository.existsById(any(Long.class))).willReturn(true);
    given(teacherDetailsRepository.findByUserId(any(Long.class)))
        .willReturn(Optional.of(teacherDetails));

    // Call method
    TeacherDetails testTeacherDetails = userService.getTeacherDetails(1L);

    // Assertions
    Assertions.assertEquals(teacherDetails, testTeacherDetails);
    verify(userRepository).existsById(any(Long.class));
    verify(teacherDetailsRepository).findByUserId(1L);
  }

  @Test
  public void getTeacherDetailsThrowsEntityNotFound() {
    // Set up test data
    given(userRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getTeacherDetails(1L));
  }

  // *** GetById ***//

  @Test
  public void getByIdHappyPath() {
    // Set up test data
    UserDto dto = Mockito.mock(UserDto.class);

    CommonTests.serviceGetById(
        User.class,
        user,
        dto,
        userRepository::findById,
        userMapper::convertToDto,
        userService::getById);
  }

  @Test
  public void getByIdThrowsEntityNotFound() {
    given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));
  }

  // *** GetByEmail ***//

  @Test
  public void getByEmailHappyPath() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

    User testUser = userService.getByEmail("email@example.com");

    Assertions.assertEquals(user, testUser);
  }

  @Test
  public void getByEmailThrowsEntityNotFound() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> userService.getByEmail("email@example/com"));
  }
}
