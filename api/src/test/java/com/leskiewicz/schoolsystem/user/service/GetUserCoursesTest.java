package com.leskiewicz.schoolsystem.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class GetUserCoursesTest {

  // Mocks
  @Mock private CourseRepository courseRepository;
  @Mock private UserRepository userRepository;
  @Mock private CourseMapper courseMapper;
  @InjectMocks private UserServiceImpl userService;

  @Test
  public void getUserCoursesReturnsPagedCourses() {
    Faculty faculty = TestHelper.createFaculty();
    User teacher = TestHelper.createTeacher(faculty);
    List<Course> courseList = TestHelper.createCoursesList(faculty, teacher);
    Page<Course> coursePage = new PageImpl<>(courseList);

    // Mock repository
    given(courseRepository.findCoursesByUserId(any(Long.class), any(Pageable.class)))
        .willReturn(coursePage);

    // Mock the behavior of the courseMapper
    CourseDto courseDto1 = TestHelper.createCourseDto(courseList.get(0));
    CourseDto courseDto2 = TestHelper.createCourseDto(courseList.get(1));

    given(courseMapper.convertToDto(any(Course.class))).willReturn(courseDto1, courseDto2);
    given(userRepository.existsById(any(Long.class))).willReturn(true);

    // Call the method to test
    Page<CourseDto> result = userService.getUserCourses(1L, PageRequest.of(0, 10));

    // Assert the result
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(courseDto1, result.getContent().get(0));
    Assertions.assertEquals(courseDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(courseRepository, times(1)).findCoursesByUserId(any(Long.class), any(Pageable.class));
    verify(courseMapper, times(2)).convertToDto(any(Course.class));
  }

  @Test
  public void getUserCoursesReturns404IfUserDoesntExist() {
    Pageable pageable = Mockito.mock(PageRequest.class);

    given(userRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> userService.getUserCourses(1L, pageable));
  }
}
