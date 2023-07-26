package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PagedResourcesAssembler;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

  // Used to convert DTOs to HAL representations
  private CourseDtoAssembler courseDtoAssembler;

  // Used to add links to paged resources
  private PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  private CourseService courseService;

  private CourseController courseController;

  @BeforeEach
  public void setup() {
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    courseService = Mockito.mock(CourseService.class);

    courseController =
        new CourseController(courseService, courseDtoAssembler, coursePagedResourcesAssembler);
  }

  @Test
  public void getCourses() {
    CommonTests.controllerGetEntities(
        CourseDto.class,
        coursePagedResourcesAssembler,
        courseService::getCourses,
        courseDtoAssembler::toModel,
        courseController::getCourses);
  }

  @Test
  public void getCourseById() {
    CourseDto courseDto = TestHelper.createCourseDto("faculty", "teacher name");

    CommonTests.controllerGetEntityById(
        courseDto,
        1L,
        courseService::getById,
        courseDtoAssembler::toModel,
        courseController::getCourseById);
  }
}
