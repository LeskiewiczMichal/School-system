package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapperImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CourseMapperTest {

  // Variables
  Faculty faculty;
  Course course;
  User teacher;

  @InjectMocks private CourseMapperImpl courseMapper;

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
            .durationInHours(course.getDuration_in_hours())
            .build();

    CourseDto result = courseMapper.convertToDto(course);

    Assertions.assertEquals(expectedCourseDto, result);
  }
}
