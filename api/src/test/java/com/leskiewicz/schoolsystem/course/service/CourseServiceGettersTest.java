package com.leskiewicz.schoolsystem.course.service;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.CourseServiceImpl;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CourseServiceGettersTest {

  // Repositories
  @Mock private CourseRepository courseRepository;
  @Mock private UserRepository userRepository;

  // Mappers
  @Mock private CourseMapper courseMapper;
  @Mock private UserMapper userMapper;

  @InjectMocks private CourseServiceImpl courseService;

  @Test
  public void getCoursesReturnsPagedCourses() {
    // Prepare test data
    Faculty faculty = TestHelper.createFaculty();
    User teacher = TestHelper.createTeacher(faculty);
    List<Course> courses =
        List.of(
            TestHelper.createCourse(faculty, teacher), TestHelper.createCourse(faculty, teacher));
    List<CourseDto> courseDtos =
        List.of(
            TestHelper.createCourseDto(courses.get(0)), TestHelper.createCourseDto(courses.get(1)));

    CommonTests.serviceGetAll(
        Course.class,
        courses,
        courseDtos,
        courseRepository::findAll,
        courseMapper::convertToDto,
        courseService::getCourses);
  }

  @Test
  public void getCourseByIdReturnsCorrectCourse() {
    // Prepare test data
    Faculty faculty = TestHelper.createFaculty();
    User teacher = TestHelper.createTeacher(faculty);
    Course course = TestHelper.createCourse(faculty, teacher);
    CourseDto courseDto = TestHelper.createCourseDto(course);

    CommonTests.serviceGetById(
        Course.class,
        course,
        courseDto,
        courseRepository::findById,
        courseMapper::convertToDto,
        courseService::getById);
  }

  @Test
  public void getCourseUsersReturnsPagedUsers() {
    // Prepare test data
    Faculty faculty = TestHelper.createFaculty();
    Degree degree = TestHelper.createDegree(faculty);
    List<User> users =
        List.of(TestHelper.createUser(faculty, degree), TestHelper.createUser(faculty, degree));
    List<UserDto> userDtos =
        List.of(TestHelper.createUserDto(users.get(0)), TestHelper.createUserDto(users.get(1)));

    CommonTests.serviceGetAllResourcesRelated(
        User.class,
        users,
        userDtos,
        userRepository::findUsersByCourseId,
        userMapper::convertToDto,
        courseRepository::existsById,
        courseService::getCourseStudents);
  }

  @Test
  public void getCourseDescription_returnsCorrectDescription() {
    // Prepare test data
    String description = "description";

    // Mocks
    given(courseRepository.existsById(any(Long.class))).willReturn(true);
    Mockito.when(courseRepository.findCourseDescriptionById(any(Long.class))).thenReturn(description);

    // Execute service call
    String result = courseService.getCourseDescription(1L);

    // Assert result
    assert result.equals(description);
  }
}
