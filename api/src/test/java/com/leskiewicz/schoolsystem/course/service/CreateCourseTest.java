package com.leskiewicz.schoolsystem.course.service;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.CourseServiceImpl;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateCourseTest {

  @Mock private CourseRepository courseRepository;
  @Mock private FacultyRepository facultyRepository;
  @Mock private UserRepository userRepository;
  @Mock private CourseMapper courseMapper;

  @InjectMocks private CourseServiceImpl courseService;

  User teacher;
  Faculty faculty;

  @BeforeEach
  public void setup() {
    teacher = Mockito.mock(User.class);
    faculty = Mockito.mock(Faculty.class);
  }

  @Test
  public void createsAndReturnCourseOnProperRequest() {
    // Mock data
    CourseDto courseDto = TestHelper.createCourseDto("Course Title", "Teacher Name");
    given(teacher.getRole()).willReturn(Role.ROLE_TEACHER);

    // Create course that should be saved in the repository
    Course expectedSavedCourse =
        Course.builder()
            .duration_in_hours(10)
            .title("Course Title")
            .teacher(teacher)
            .faculty(faculty)
            .build();

    // Create a mock request
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title(expectedSavedCourse.getTitle())
            .durationInHours(expectedSavedCourse.getDuration_in_hours())
            .facultyId(1L)
            .teacherId(1L)
            .build();

    // Mock the behaviour of repositories and mappers
    given(facultyRepository.findById(any(Long.class))).willReturn(Optional.of(faculty));
    given(userRepository.findById(any(Long.class))).willReturn(Optional.of(teacher));
    given(
            courseRepository.existsCourseWithAttributes(
                any(String.class), any(Integer.class), any(Long.class), any(Long.class)))
        .willReturn(false);
    given(courseMapper.convertToDto(any())).willReturn(courseDto);

    // Call the method
    CourseDto createdCourse = courseService.createCourse(request);

    // Verify the results
    Assertions.assertEquals(courseDto, createdCourse);

    // Course was saved in the repository
    ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
    verify(courseRepository).save(courseCaptor.capture());
    Course savedCourse = courseCaptor.getValue();
    Assertions.assertEquals(expectedSavedCourse, savedCourse);

    // Verify calls
    verify(courseRepository, Mockito.times(1)).save(any());
    verify(facultyRepository, Mockito.times(1)).findById(any());
    verify(userRepository, Mockito.times(1)).findById(any());
    verify(courseRepository, Mockito.times(1))
        .existsCourseWithAttributes(any(), anyInt(), any(), any());
    verify(courseMapper, Mockito.times(1)).convertToDto(any());
  }

  @Test
  public void createCourseThrowsIllegalArgumentExceptionWhenUserIsNotTeacher() {
    // Set up data
    given(userRepository.findById(any(Long.class))).willReturn(Optional.of(teacher));
    given(teacher.getRole()).willReturn(Role.ROLE_STUDENT);

    // Create request
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Course Title")
            .durationInHours(10)
            .facultyId(1L)
            .teacherId(1L)
            .build();

    // Verify Error
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          courseService.createCourse(request);
        });
  }

  @Test
  public void createCourseThrowsEntityNotFoundExceptionWhenFacultyDoesNotExist() {
    // Set up data
    given(userRepository.findById(any(Long.class))).willReturn(Optional.of(teacher));
    given(teacher.getRole()).willReturn(Role.ROLE_TEACHER);
    given(facultyRepository.findById(any(Long.class))).willReturn(Optional.empty());

    // Create request
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Course Title")
            .durationInHours(10)
            .facultyId(1L)
            .teacherId(1L)
            .build();

    // Verify Error
    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> {
          courseService.createCourse(request);
        });
  }

  @Test
  public void createCourseThrowsEntityNotFoundExceptionWhenUserDoesNotExist() {
    // Set up data
    given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());

    // Create request
    CreateCourseRequest request =
        CreateCourseRequest.builder()
            .title("Course Title")
            .durationInHours(10)
            .facultyId(1L)
            .teacherId(1L)
            .build();

    // Verify Error
    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> {
          courseService.createCourse(request);
        });
  }
}
