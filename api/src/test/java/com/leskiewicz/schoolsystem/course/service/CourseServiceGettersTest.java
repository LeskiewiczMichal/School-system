package com.leskiewicz.schoolsystem.course.service;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.CourseServiceImpl;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
  public void searchReturnsPagedDegrees() {
    // Set up test data
    List<Course> courseList =
            Arrays.asList(
                    TestHelper.createCourse(Mockito.mock(Faculty.class), Mockito.mock(User.class)),
                    TestHelper.createCourse(Mockito.mock(Faculty.class), Mockito.mock(User.class)));
    List<CourseDto> courseDtoList =
            Arrays.asList(
                    TestHelper.createCourseDto("Informatics", "John Doe"),
                    TestHelper.createCourseDto("Biology", "John Doe"));
    Page<Course> coursePage = new PageImpl<>(courseList);

    // Mocks
    given(
            courseRepository.searchByFacultyIdAndTitleAndLanguage(any(String.class), any(Long.class), any(Language.class), any(Pageable.class))
    ).willReturn(coursePage);
    given(courseMapper.convertToDto(any(Course.class))).willReturn(courseDtoList.get(0), courseDtoList.get(1));

    // Call method
    Page<CourseDto> result = courseService.search("qwer", 1L, Language.ENGLISH, PageRequest.of(0, 2));

    // Assertions
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(2, result.getNumberOfElements());
    Assertions.assertEquals(0, result.getNumber());
    Assertions.assertEquals(2, result.getSize());
    Assertions.assertEquals(courseDtoList.get(0), result.getContent().get(0));
    Assertions.assertEquals(courseDtoList.get(1), result.getContent().get(1));

    verify(courseRepository).searchByFacultyIdAndTitleAndLanguage("qwer", 1L, Language.ENGLISH, PageRequest.of(0, 2));
    verify(courseMapper).convertToDto(courseList.get(0));
    verify(courseMapper).convertToDto(courseList.get(1));
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
