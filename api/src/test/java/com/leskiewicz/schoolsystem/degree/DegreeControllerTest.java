package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@ExtendWith(MockitoExtension.class)
public class DegreeControllerTest {

  private DegreeService degreeService;

  // Assemblers
  private DegreeDtoAssembler degreeDtoAssembler;
  private CourseDtoAssembler courseDtoAssembler;
  private PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  private PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  DegreeController degreeController;

  // Annotation mocks didn't work as expected here, pagedResourcesAssemblers were mixed up
  @BeforeEach
  public void setUp() {
    // Create mock instances
    degreeService = Mockito.mock(DegreeService.class);
    degreeDtoAssembler = Mockito.mock(DegreeDtoAssembler.class);
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    degreePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);

    // Create DegreeController instance
    degreeController =
        new DegreeController(
            degreeService,
            degreeDtoAssembler,
            courseDtoAssembler,
            degreePagedResourcesAssembler,
            coursePagedResourcesAssembler);
  }

  @Test
  public void getDegrees() {
    CommonTests.controllerGetEntities(
        DegreeDto.class,
        degreePagedResourcesAssembler,
        degreeService::getDegrees,
        degreeDtoAssembler::toModel,
        degreeController::getDegrees);
  }

  @Test
  public void getDegreeById() {
    DegreeDto degreeDto = TestHelper.createDegreeDto("Faculty");

    CommonTests.controllerGetEntityById(
        degreeDto,
        1L,
        degreeService::getById,
        degreeDtoAssembler::toModel,
        degreeController::getDegreeById);
  }

  @Test
  public void createDegree() {
    DegreeDto degreeDto = TestHelper.createDegreeDto("Faculty");

    CommonTests.controllerCreateEntity(
        degreeDto,
        DegreeDto.class,
        degreeDto.add(WebMvcLinkBuilder.linkTo(DegreeController.class).withSelfRel()),
        CreateDegreeRequest.class,
        new CreateDegreeRequest(degreeDto.getTitle(), degreeDto.getFieldOfStudy(), degreeDto.getFaculty()),
        degreeService::createDegree,
        degreeDtoAssembler::toModel,
        degreeController::createDegree);
  }
}
