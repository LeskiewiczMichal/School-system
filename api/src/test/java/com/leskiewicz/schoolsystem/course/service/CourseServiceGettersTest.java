package com.leskiewicz.schoolsystem.course.service;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.CourseServiceImpl;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CourseServiceGettersTest {

  // Repositories
  @Mock private CourseRepository courseRepository;

  // Mappers
  @Mock private CourseMapper courseMapper;

  @InjectMocks private CourseServiceImpl courseService;

  @Test
  public void getCoursesReturnsPagedCourses() {
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
}
