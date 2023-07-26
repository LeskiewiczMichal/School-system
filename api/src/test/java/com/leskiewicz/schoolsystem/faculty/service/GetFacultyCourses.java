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
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
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

  // Mocks

  @Mock private CourseRepository courseRepository;
  @Mock private FacultyRepository facultyRepository;
  @Mock private CourseMapper courseMapper;
  @InjectMocks private FacultyServiceImpl facultyService;

  @Test
  public void getFacultyCoursesReturnsPagedCourses() {
    Faculty faculty = Mockito.mock(Faculty.class);
    User teacher = Mockito.mock(User.class);
    List<Course> courses =
        Arrays.asList(
            TestHelper.createCourse(faculty, teacher), TestHelper.createCourse(faculty, teacher));
    List<CourseDto> courseDtos =
        Arrays.asList(
            TestHelper.createCourseDto("Test", "Teacher name"),
            TestHelper.createCourseDto("Testing", "Name Teacher"));

    CommonTests.serviceGetAllResourcesRelated(
        Course.class,
        courses,
        courseDtos,
        courseRepository::findCoursesByFacultyId,
        courseMapper::convertToDto,
        facultyRepository::existsById,
        facultyService::getFacultyCourses);
  }

  @Test
  public void getFacultyDegreesReturns404IfFacultyDoesntExist() {
    Pageable pageable = Mockito.mock(PageRequest.class);

    given(facultyRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> facultyService.getFacultyCourses(1L, pageable));
  }
}
