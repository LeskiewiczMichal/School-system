package com.leskiewicz.schoolsystem.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.authentication.Role;
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
import com.leskiewicz.schoolsystem.utils.Language;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    given(userRepository.findById(any(Long.class))).willReturn(Optional.of(teacher));

    CommonTests.serviceGetAllResourcesRelated(
        Course.class,
        courseList,
        courseDtos,
        courseRepository::findCoursesByTeacherId,
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
  public void searchReturnsPagedUsers() {
    // Set up test data
    List<User> usersList =
        Arrays.asList(
            TestHelper.createUser(faculty, degree), TestHelper.createUser(faculty, degree));
    List<UserDto> userDtos =
        Arrays.asList(
            TestHelper.createUserDto("FacultyTest", "DegreeTest"),
            TestHelper.createUserDto("TestFaculty", "TestDegree"));
    Page<User> userPage = new PageImpl<>(usersList);

    // Mocks
    given(userRepository.searchUsersByLastNameAndFirstNameAndRole(any(String.class), any(String.class), any(Role.class), any(Pageable.class)))
        .willReturn(userPage);
    given(userMapper.convertToDto(any(User.class))).willReturn(userDtos.get(0), userDtos.get(1));

    // Call method
    Page<UserDto> result = userService.search("Test", "Test", Role.ROLE_TEACHER, PageRequest.of(0, 2));

    // Assertions
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(2, result.getNumberOfElements());
    Assertions.assertEquals(0, result.getNumber());
    Assertions.assertEquals(2, result.getSize());
    Assertions.assertEquals(userDtos.get(0), result.getContent().get(0));
    Assertions.assertEquals(userDtos.get(1), result.getContent().get(1));

    verify(userRepository)
        .searchUsersByLastNameAndFirstNameAndRole(
            "Test", "Test", Role.ROLE_TEACHER, PageRequest.of(0, 2));
    verify(userMapper, times(2)).convertToDto(any(User.class));
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
