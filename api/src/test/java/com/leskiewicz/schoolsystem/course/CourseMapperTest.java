package com.leskiewicz.schoolsystem.course;

import static com.leskiewicz.schoolsystem.builders.CourseBuilder.aCourse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapperImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
public class CourseMapperTest {

  // Variables
  Faculty faculty;
  Course course;
  User teacher;

  @InjectMocks private CourseMapperImpl courseMapper;

  static Stream<Arguments> throwsConstraintViolationExceptionOnInvalidFacultyProvider() {
    User teacher = TestHelper.createTeacher(null);
    Faculty faculty = TestHelper.createFaculty();
    Course testCourse = TestHelper.createCourse(faculty, teacher);

    Arguments testCourseWithNullFaculty =
        Arguments.of(testCourse.toBuilder().faculty(null).build());
    Arguments testCourseWithNullTeacher =
        Arguments.of(testCourse.toBuilder().teacher(null).build());
    Arguments testCourseWithNullTitle = Arguments.of(testCourse.toBuilder().title(null).build());

    return Stream.of(testCourseWithNullFaculty, testCourseWithNullTeacher, testCourseWithNullTitle);
  }

  @BeforeEach
  public void setup() {
    faculty = TestHelper.createFaculty();
    teacher = TestHelper.createTeacher(faculty);
    course = TestHelper.createCourse(faculty, teacher);
  }

  @Test
  public void convertToDtoCorrectForCourse() {
    CourseDto expectedCourseDto =
        CourseDto.builder()
            .id(1L)
            .title(course.getTitle())
            .teacher(teacher.getFirstName() + " " + teacher.getLastName())
            .faculty(faculty.getName())
            .teacherId(teacher.getId())
            .facultyId(faculty.getId())
            .language(course.getLanguage())
            .scope(course.getScope())
            .durationInHours(course.getDuration_in_hours())
            .ECTS(course.getECTS())
            .build();

    CourseDto result = courseMapper.convertToDto(course);

    Assertions.assertEquals(expectedCourseDto, result);
  }

  @Test
  public void convertToDtoThrowsIllegalArgumentExceptionOnNoId() {
    course.setId(null);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> courseMapper.convertToDto(course));
  }

  @ParameterizedTest
  @MethodSource("throwsConstraintViolationExceptionOnInvalidFacultyProvider")
  public void throwsConstraintViolationExceptionOnInvalidFaculty(Course testCourse) {
    Assertions.assertThrows(
        ConstraintViolationException.class, () -> courseMapper.convertToDto(testCourse));
  }

  @Test
  public void mapPageToDtoMapsCoursesProperly() {
    Course course = aCourse().title("test").build();
    Page<Course> coursesPage = new PageImpl<>(List.of(course));

    Page<CourseDto> result = courseMapper.mapPageToDto(coursesPage);

    Assertions.assertEquals(1, result.getTotalElements());
    Assertions.assertEquals("test", result.getContent().get(0).getTitle());
  }
}
