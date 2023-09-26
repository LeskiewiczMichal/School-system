package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.DefaultExceptionHandler;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyDtoAssembler;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.leskiewicz.schoolsystem.builders.CourseBuilder.*;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.aDegree;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.degreeDtoFrom;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.aFaculty;
import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.facultyDtoFrom;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FacultyControllerTest {
  private static final String facultiesLink = "/api/faculties";

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

  private MockMvc mvc;
  List<Faculty> facultiesList =
      List.of(aFaculty().build(), aFaculty().name("Test faculty").build());
  List<FacultyDto> facultyDtosList =
      List.of(facultyDtoFrom(facultiesList.get(0)), facultyDtoFrom(facultiesList.get(1)));

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

    mvc =
        MockMvcBuilders.standaloneSetup(facultyController)
            .setControllerAdvice(new DefaultExceptionHandler())
            .build();
  }

  @Test
  public void getFacultiesReturnsFormattedResponse() throws Exception {
    Page<FacultyDto> facultyDtoPage = new PageImpl<>(facultyDtosList);
    PagedModel<FacultyDto> facultyPagedModel =
        PagedModel.of(facultyDtosList, new PagedModel.PageMetadata(1, 1, 1, 1));

    when(facultyService.getFaculties(new PageableRequest().toPageable()))
        .thenReturn(facultyDtoPage);
    when(facultyDtoAssembler.mapPageToModel(facultyDtoPage))
        .thenReturn(new PageImpl<>(facultyDtosList));
    when(facultyPagedResourcesAssembler.toModel(any(Page.class))).thenReturn(facultyPagedModel);

    mvc.perform(get(facultiesLink).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.links").isArray())
        .andReturn();
  }

  @Test
  public void getFacultyByIdReturnsFormattedResponse() {
    FacultyDto facultyDto = facultyDtoFrom(aFaculty().build());

    when(facultyService.getById(1L)).thenReturn(facultyDto);
    when(facultyDtoAssembler.toModel(facultyDto)).thenReturn(facultyDto);

    ResponseEntity<FacultyDto> result = facultyController.getFacultyById(1L);

    Assertions.assertEquals(facultyDto, result.getBody());
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Nested
  public class patchFaculty {
    PatchFacultyRequest request = Mockito.mock(PatchFacultyRequest.class);

    @Test
    public void returnsFacultyDto() {
      FacultyDto facultyDto = setUpPatchFaculty();

      ResponseEntity<FacultyDto> response =
          facultyController.updateFaculty(request, facultyDto.getId());

      Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
      Assertions.assertEquals(facultyDto, response.getBody());
    }

    @Test
    public void callsUpdateFaculty() {
      FacultyDto facultyDto = setUpPatchFaculty();
      Long id = facultyDto.getId();

      facultyController.updateFaculty(request, id);

      verify(facultyService).updateFaculty(request, id);
    }

    private FacultyDto setUpPatchFaculty() {
      FacultyDto facultyDto = facultyDtoFrom(aFaculty().build());
      when(facultyService.updateFaculty(request, facultyDto.getId())).thenReturn(facultyDto);
      when(facultyDtoAssembler.toModel(any(FacultyDto.class))).thenReturn(facultyDto);

      return facultyDto;
    }
  }

  @Test
  public void getFacultyDegrees() {
    List<DegreeDto> degreeDtoList =
        List.of(
            degreeDtoFrom(aDegree().build()),
            degreeDtoFrom(aDegree().fieldOfStudy("Testing").build()));
    Page<DegreeDto> degreeDtosPage = new PageImpl<>(degreeDtoList);
    PagedModel<EntityModel<DegreeDto>> pagedModel = Mockito.mock(PagedModel.class);

    when(facultyService.getFacultyDegrees(1L, new PageableRequest().toPageable()))
        .thenReturn(degreeDtosPage);
    when(degreeDtoAssembler.toModel(any(DegreeDto.class)))
        .thenReturn(degreeDtoList.get(0), degreeDtoList.get(1));
    when(degreePagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

    ResponseEntity<RepresentationModel<DegreeDto>> response =
        facultyController.getFacultyDegrees(1L, new PageableRequest());

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(HalModelBuilder.halModelOf(pagedModel).build(), response.getBody());
  }

  @Test
  public void getFacultyCoursesReturnsCourseDtos() {
    List<CourseDto> courseDtosList = createCourseDtoListFrom(createCourseList());
    Page<CourseDto> courseDtosPage = new PageImpl<>(courseDtosList);
    PagedModel<EntityModel<CourseDto>> pagedModel = Mockito.mock(PagedModel.class);

    when(facultyService.getFacultyCourses(any(Long.class), any(Pageable.class)))
        .thenReturn(courseDtosPage);
    when(courseDtoAssembler.toModel(any(CourseDto.class)))
        .thenReturn(courseDtosList.get(0), courseDtosList.get(1));
    when(coursePagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

    ResponseEntity<RepresentationModel<CourseDto>> response =
        facultyController.getFacultyCourses(1L, new PageableRequest());

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(HalModelBuilder.halModelOf(pagedModel).build(), response.getBody());
  }

  @Test
  public void getFacultyStudents() {
    CommonTests.controllerGetEntities(
        UserDto.class,
        userPagedResourcesAssembler,
        (Pageable pageable) -> facultyService.getFacultyUsers(1L, pageable, Role.ROLE_STUDENT),
        userDtoAssembler::toModel,
        (PageableRequest request) -> facultyController.getFacultyStudents(1L, request));
  }

  @Test
  public void getFacultyTeachers() {
    CommonTests.controllerGetEntities(
        UserDto.class,
        userPagedResourcesAssembler,
        (Pageable pageable) -> facultyService.getFacultyUsers(1L, pageable, Role.ROLE_TEACHER),
        userDtoAssembler::toModel,
        (PageableRequest request) -> facultyController.getFacultyTeachers(1L, request));
  }

  @Test
  public void testCreateFaculty() {
    FacultyDto mockedFacultyDto = TestHelper.createFacultyDto("TestFaculty");

    CommonTests.controllerCreateEntity(
        mockedFacultyDto,
        FacultyDto.class,
        mockedFacultyDto.add(WebMvcLinkBuilder.linkTo(CreateFacultyRequest.class).withSelfRel()),
        CreateFacultyRequest.class,
        new CreateFacultyRequest("TestFaculty"),
        facultyService::createFaculty,
        facultyDtoAssembler::toModel,
        facultyController::createFaculty);
  }
}
