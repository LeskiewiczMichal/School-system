package com.leskiewicz.schoolsystem.faculty.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class GetFacultyCourses {

  // Variables
  Faculty faculty;

  // Mocks
  @Mock private CourseRepository courseRepository;
  @Mock private FacultyRepository facultyRepository;
  @Mock private CourseMapper courseMapper;
  @InjectMocks private FacultyServiceImpl facultyService;

  @BeforeEach
  public void setup() {

    // Set up test data
    faculty = Faculty.builder().id(1L).name("Software Engineering").build();
  }

  @Test
  public void getFacultyDegreesReturnsPagedDegrees() {
    User teacher = Mockito.mock(User.class);

    List<Course> facultyList =
        Arrays.asList(
            Course.builder()
                .id(1L)
                .title("Software Engineering")
                .faculty(faculty)
                .duration_in_hours(20)
                .teacher(teacher)
                .build(),
            Course.builder()
                .id(2L)
                .title("Computer Science")
                .faculty(faculty)
                .duration_in_hours(30)
                .teacher(teacher)
                .build());
    Page<Course> coursePage = new PageImpl<>(facultyList);

    given(courseRepository.findCoursesByFacultyId(any(Long.class), any(Pageable.class)))
        .willReturn(coursePage);

    // Mock the behavior of the courseMapper
    CourseDto courseDto1 =
        CourseDto.builder()
            .id(1L)
            .title("Software Engineering")
            .faculty("faculty")
            .teacher("Something Testing")
            .durationInHours(20)
            .build();
    CourseDto courseDto2 =
        CourseDto.builder()
            .id(2L)
            .title("Computer Science")
            .faculty("faculty")
            .teacher("Something Testing")
            .durationInHours(30)
            .build();

    given(courseMapper.convertToDto(any(Course.class))).willReturn(courseDto1, courseDto2);
    given(facultyRepository.existsById(any(Long.class))).willReturn(true);

    // Call the method to test
    Page<CourseDto> result = facultyService.getFacultyCourses(1L, PageRequest.of(0, 10));

    // Assert the result
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(courseDto1, result.getContent().get(0));
    Assertions.assertEquals(courseDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(courseRepository, times(1)).findCoursesByFacultyId(any(Long.class), any(Pageable.class));
    verify(courseMapper, times(2)).convertToDto(any(Course.class));
  }

  @Test
  public void getFacultyDegreesReturns404IfFacultyDoesntExist() {
    Pageable pageable = Mockito.mock(PageRequest.class);

    given(facultyRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> facultyService.getFacultyCourses(faculty.getId(), pageable));
  }
}
