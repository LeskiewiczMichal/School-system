package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyDtoAssembler;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;

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

  @Test
  public void patchFaculty() {
    CommonTests.controllerPatchEntity(
            FacultyDto.class,
            PatchFacultyRequest.class,
            facultyService::updateFaculty,
            facultyDtoAssembler::toModel,
            facultyController::updateFaculty
    );
  }

  @Test
  public void getFacultyDegrees() {
    CommonTests.controllerGetEntities(
            DegreeDto.class,
            degreePagedResourcesAssembler,
            (Pageable pageable) ->  facultyService.getFacultyDegrees(1L, pageable),
            degreeDtoAssembler::toModel,
            (PageableRequest request) -> facultyController.getFacultyDegrees(1L, request)
    );
  }

  @Test
  public void getFacultyCourses() {
    CommonTests.controllerGetEntities(
            CourseDto.class,
            coursePagedResourcesAssembler,
            (Pageable pageable) ->  facultyService.getFacultyCourses(1L, pageable),
            courseDtoAssembler::toModel,
            (PageableRequest request) -> facultyController.getFacultyCourses(1L, request)
    );
  }

  @Test
  public void getFacultyStudents() {
    CommonTests.controllerGetEntities(
            UserDto.class,
            userPagedResourcesAssembler,
            (Pageable pageable) ->  facultyService.getFacultyUsers(1L, pageable, Role.ROLE_STUDENT),
            userDtoAssembler::toModel,
            (PageableRequest request) -> facultyController.getFacultyStudents(1L, request)
    );
  }

  
}
