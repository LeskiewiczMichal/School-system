package com.leskiewicz.schoolsystem.user;

import static com.leskiewicz.schoolsystem.builders.CourseBuilder.*;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.aDegree;
import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.aFaculty;
import static com.leskiewicz.schoolsystem.builders.TeacherDetailsBuilder.aTeacherDetails;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.anUser;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.userDtoFrom;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.DefaultExceptionHandler;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.PatchTeacherDetailsRequest;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsModelAssembler;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.hamcrest.Matchers;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
  private static final String teacherDetailsLink = "/api/users/1/teacher-details";
  private MockMvc mvc;

  Faculty specialFaculty = aFaculty().name("Special Faculty").build();
  Degree specialDegree = aDegree().title(DegreeTitle.PROFESSOR).faculty(specialFaculty).build();
  List<UserDto> userDtosList =
      List.of(
          userDtoFrom(anUser().build()),
          userDtoFrom(anUser().faculty(specialFaculty).degree(specialDegree).build()));
  Page<UserDto> userDtosPage = new PageImpl<>(userDtosList);
  PagedModel<UserDto> userPagedModel =
      PagedModel.of(userDtosList, new PagedModel.PageMetadata(1, 1, 1, 1));

  // Mocks
  private UserService userService;
  private UserDtoAssembler userDtoAssembler;
  private PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;
  private CourseDtoAssembler courseDtoAssembler;
  private PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;
  private TeacherDetailsModelAssembler teacherDetailsModelAssembler;

  private UserController userController;
  ObjectMapper objectMapper =
      new ObjectMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .registerModule(new JavaTimeModule());

  // Annotation mocks didn't work as expected here, pagedResourcesAssemblers were mixed up
  @BeforeEach
  public void setUp() {
    userService = Mockito.mock(UserService.class);
    userDtoAssembler = Mockito.mock(UserDtoAssembler.class);
    userPagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    teacherDetailsModelAssembler = Mockito.mock(TeacherDetailsModelAssembler.class);

    userController =
        new UserController(
            userService,
            userDtoAssembler,
            courseDtoAssembler,
            teacherDetailsModelAssembler,
            userPagedResourcesAssembler,
            coursePagedResourcesAssembler);

    mvc =
        MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(new DefaultExceptionHandler())
            .build();
  }

  @Test
  public void getUserByIdReturnsFormattedResponse() {
    UserDto userDto = userDtoFrom(anUser().build());

    when(userService.getById(userDto.getId())).thenReturn(userDto);
    when(userDtoAssembler.toModel(userDto)).thenReturn(userDto);

    ResponseEntity<UserDto> result = userController.getUserById(userDto.getId());

    Assertions.assertEquals(userDto, result.getBody());
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  public void getUsersReturnsFormattedResponse() throws Exception {
    when(userService.getUsers(new PageableRequest().toPageable())).thenReturn(userDtosPage);
    when(userDtoAssembler.mapPageToModel(userDtosPage)).thenReturn(userDtosPage);
    when(userPagedResourcesAssembler.toModel(any(Page.class))).thenReturn(userPagedModel);

    mvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.links").isArray())
        .andExpect(jsonPath("$.links[0].rel").value("self"))
        .andExpect(jsonPath("$.links[1].rel").value("profilePicture"))
        .andReturn();
  }

  @Test
  public void searchUsersReturnsFormattedResponse() throws Exception {
    setUpSearchTest();
    ResultActions result = makeSearchRequest();
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.links").isArray())
        .andExpect(jsonPath("$.links[0].rel").value("self"))
        .andExpect(jsonPath("$.links[1].rel").value("users"))
        .andExpect(jsonPath("$.links[2].rel").value("user"))
        .andReturn();
  }

  @Test
  public void searchUsersUsesNeededFunctions() throws Exception {
    setUpSearchTest();
    makeSearchRequest();
    verify(userService, times(1))
        .search(any(String.class), any(String.class), any(Role.class), any(Pageable.class));
    verify(userDtoAssembler).mapPageToModel(any(Page.class));
    verify(userPagedResourcesAssembler, times(1)).toModel(any(Page.class));
  }

  private void setUpSearchTest() {
    when(userService.search(
            any(String.class), any(String.class), any(Role.class), any(Pageable.class)))
        .thenReturn(userDtosPage);
    when(userDtoAssembler.mapPageToModel(any(Page.class))).thenReturn(userDtosPage);
    when(userPagedResourcesAssembler.toModel(any(Page.class))).thenReturn(userPagedModel);
  }

  private ResultActions makeSearchRequest() throws Exception {
    return mvc.perform(
        get("/api/users/search")
            .param("firstName", "Test")
            .param("lastName", "Test")
            .param("role", "ROLE_TEACHER")
            .accept(MediaType.APPLICATION_JSON));
  }

  @Test
  public void patchUserReturnsUserDto() {
    UserDto userDto = userDtoFrom(anUser().build());
    Long id = userDto.getId();
    PatchUserRequest request = Mockito.mock(PatchUserRequest.class);

    when(userService.updateUser(request, id)).thenReturn(userDto);
    when(userDtoAssembler.toModel(any(UserDto.class))).thenReturn(userDto);

    ResponseEntity<UserDto> response = userController.patchUser(request, id);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(userDto, response.getBody());
    verify(userService, times(1)).updateUser(request, id);
  }

  @Test
  public void getUserCoursesReturnsProperResponse() {
    PageableRequest request = new PageableRequest();
    List<CourseDto> courseDtosList = createCourseDtoListFrom(createCourseList());
    Page<CourseDto> courseDtosPage = new PageImpl<>(courseDtosList);
    PagedModel<EntityModel<CourseDto>> pagedModel = Mockito.mock(PagedModel.class);

    when(userService.getUserCourses(1L, request.toPageable())).thenReturn(courseDtosPage);
    when(courseDtoAssembler.toModel(any(CourseDto.class)))
        .thenReturn(courseDtosList.get(0), courseDtosList.get(1));
    when(coursePagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

    ResponseEntity<RepresentationModel<CourseDto>> response =
        userController.getUserCourses(1L, request);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(HalModelBuilder.halModelOf(pagedModel).build(), response.getBody());
  }

  @Test
  public void getTeacherDetailsReturnsFormattedResponse() throws Exception {
    TeacherDetails teacherDetails = aTeacherDetails().build();

    given(userService.getTeacherDetails(any(Long.class))).willReturn(teacherDetails);
    given(teacherDetailsModelAssembler.toModel(any(TeacherDetails.class))).willCallRealMethod();

    ResultActions result = makeGetTeacherDetailsRequest();

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bio").value(teacherDetails.getBio()))
        .andExpect(jsonPath("$.tutorship").value(teacherDetails.getTutorship()))
        .andExpect(jsonPath("$.title").value(teacherDetails.getTitle().toString()))
        .andExpect(jsonPath("$.degreeField").value(teacherDetails.getDegreeField()))
        .andExpect(jsonPath("$.links[*].rel", Matchers.hasItems("self", "teacher")))
        .andExpect(jsonPath("$.links[*].href", Matchers.hasSize(2)))
        .andReturn();
  }

  @Test
  public void getTeacherDetailsThrowsProperEntityNotFoundException() throws Exception {
    willThrow(new EntityNotFoundException(ErrorMessages.teacherDetailsNotFound(1L)))
        .given(userService)
        .getTeacherDetails(any(Long.class));

    ResultActions result = makeGetTeacherDetailsRequest();

    result
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(ErrorMessages.teacherDetailsNotFound(1L)))
        .andExpect(jsonPath("$.path").value(teacherDetailsLink))
        .andExpect(jsonPath("$.statusCode").value(404));
  }

  private ResultActions makeGetTeacherDetailsRequest() throws Exception {
    return mvc.perform(
        get(teacherDetailsLink)
            .contentType(MediaType.APPLICATION_JSON)
            .accept("application/hal+json"));
  }

  @Test
  public void updateTeacherDetailsWithProperRequest() throws Exception {
    User teacher = anUser().build();
    PatchTeacherDetailsRequest request = createPatchTeacherDetailsRequest();
    TeacherDetails modifiedTeacherDetails =
        TeacherDetails.builder()
            .teacher(teacher)
            .id(1L)
            .bio(request.bio())
            .title(request.title())
            .tutorship(request.tutorship())
            .degreeField(request.degreeField())
            .build();

    given(userService.updateTeacherDetails(any(PatchTeacherDetailsRequest.class), any(Long.class)))
        .willReturn(modifiedTeacherDetails);
    given(teacherDetailsModelAssembler.toModel(any(TeacherDetails.class))).willCallRealMethod();

    ResultActions result = makePatchTeacherDetailsRequest(request);

    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bio").value(request.bio()))
        .andExpect(jsonPath("$.tutorship").value(request.tutorship()))
        .andExpect(jsonPath("$.title").value(request.title().toString()))
        .andExpect(jsonPath("$.degreeField").value(request.degreeField()))
        .andExpect(jsonPath("$.links[*].rel", Matchers.hasItems("self", "teacher")))
        .andExpect(jsonPath("$.links[*].href", Matchers.hasSize(2)))
        .andReturn();
  }

  @Test
  public void updateTeacherDetailsThrowsEntityNotFoundException() {
    PatchTeacherDetailsRequest request = createPatchTeacherDetailsRequest();

    given(userService.updateTeacherDetails(any(PatchTeacherDetailsRequest.class), any(Long.class)))
        .willThrow(new EntityNotFoundException(ErrorMessages.teacherDetailsNotFound(1L)));

    assertThrows(
        EntityNotFoundException.class,
        () -> userController.patchTeacherDetails(request, 1L),
        ErrorMessages.teacherDetailsNotFound(1L));
  }

  private ResultActions makePatchTeacherDetailsRequest(PatchTeacherDetailsRequest request)
      throws Exception {
    return mvc.perform(
        patch(teacherDetailsLink)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept("application/hal+json"));
  }

  private PatchTeacherDetailsRequest createPatchTeacherDetailsRequest() {
    return PatchTeacherDetailsRequest.builder()
        .bio("New bio")
        .title(DegreeTitle.DOCTOR)
        .tutorship("New tutorship")
        .degreeField("New degree field")
        .build();
  }

  @Test
  public void changeProfilePictureReturnsFormattedResponse() throws Exception {
    doNothing().when(userService).addImage(any(Long.class), any(MultipartFile.class));
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    mvc.perform(
            multipart("/api/users/1/profile-picture")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("File with name: test.txt uploaded successfully"))
        .andExpect(jsonPath("$.links[*].rel", Matchers.hasItems("user")))
        .andExpect(jsonPath("$.links[*].href", Matchers.hasSize(1)))
        .andReturn();
  }
}
