package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyDtoAssembler;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.UserController;
import com.leskiewicz.schoolsystem.user.UserService;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FacultyControllerTest {

  private FacultyController facultyController;

  private FacultyService facultyService;

  //     Assemblers
  private FacultyDtoAssembler facultyDtoAssembler;
  private DegreeDtoAssembler degreeDtoAssembler;
  private UserDtoAssembler userDtoAssembler;
  private CourseDtoAssembler courseDtoAssembler;
  private PagedResourcesAssembler<FacultyDto> facultyPagedResourcesAssembler;
  private PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  private PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;
  private PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  // Annotation mocks didn't work as expected here, pagedResourcesAssemblers were mixed up
  @BeforeEach
  public void setUp() {
    // Create mock instances
    facultyService = Mockito.mock(FacultyService.class);
    facultyDtoAssembler = Mockito.mock(FacultyDtoAssembler.class);
    degreeDtoAssembler = Mockito.mock(DegreeDtoAssembler.class);
    userDtoAssembler = Mockito.mock(UserDtoAssembler.class);
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    facultyPagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    degreePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    userPagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);

    // Create FacultyController instance
    facultyController =
        new FacultyController(
            facultyService,
            facultyDtoAssembler,
            degreeDtoAssembler,
            userDtoAssembler,
            courseDtoAssembler,
            facultyPagedResourcesAssembler,
            degreePagedResourcesAssembler,
            userPagedResourcesAssembler,
            coursePagedResourcesAssembler);
  }

  @Test
  public void getFaculties() {
    CommonTests.controllerGetEntities(
            FacultyDto.class,
            facultyPagedResourcesAssembler,
            facultyService::getFaculties,
            facultyDtoAssembler::toModel,
            facultyController::getFaculties
    );
  }

  @Test
  public void getFacultyById() {
    FacultyDto facultyDto = TestHelper.createFacultyDto("TestFaculty");

    CommonTests.controllerGetEntityById(
            facultyDto,
            1L,
            facultyService::getById,
            facultyDtoAssembler::toModel,
            facultyController::getFacultyById
    );
  }

}
