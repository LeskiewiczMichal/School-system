package com.leskiewicz.schoolsystem.user;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.DefaultExceptionHandler;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.PatchTeacherDetailsRequest;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsModelAssembler;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  // Services
  private UserService userService;

  // Assemblers
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

  private MockMvc mvc;

  // Annotation mocks didn't work as expected here, pagedResourcesAssemblers were mixed up
  @BeforeEach
  public void setUp() {
    // Create mock instances
    userService = Mockito.mock(UserService.class);
    userDtoAssembler = Mockito.mock(UserDtoAssembler.class);
    userPagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    teacherDetailsModelAssembler = Mockito.mock(TeacherDetailsModelAssembler.class);

    // Create UserController instance
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
  public void getUserById() {
    //    // Mock input and output data
    Faculty faculty = Mockito.mock(Faculty.class);
    Degree degree = Mockito.mock(Degree.class);
    given(faculty.getName()).willReturn("Test");
    given(degree.getFieldOfStudy()).willReturn("Law");
    UserDto userDto = TestHelper.createUserDto(faculty.getName(), degree.getFieldOfStudy());

    CommonTests.controllerGetEntityById(
        userDto, 1L, userService::getById, userDtoAssembler::toModel, userController::getUserById);
  }

  @Test
  public void getUsers() {
    CommonTests.controllerGetEntities(
        UserDto.class,
        userPagedResourcesAssembler,
        userService::getUsers,
        userDtoAssembler::toModel,
        userController::getUsers);
  }

  @Test
  public void testPatchUser() {
    CommonTests.controllerPatchEntity(
        UserDto.class,
        PatchUserRequest.class,
        userService::updateUser,
        userDtoAssembler::toModel,
        userController::patchUser);
  }

  @Test
  public void getUserCourses() {
    CommonTests.controllerGetEntities(
        CourseDto.class,
        coursePagedResourcesAssembler,
        (Pageable pageable) -> userService.getUserCourses(1L, pageable),
        courseDtoAssembler::toModel,
        (PageableRequest request) -> userController.getUserCourses(1L, request));
  }

  @Test
  public void getTeacherDetails() throws Exception {
    // Prepare test data
    User user = Mockito.mock(User.class);
    TeacherDetails teacherDetails = TestHelper.createTeacherDetails(user);
    given(userService.getTeacherDetails(any(Long.class))).willReturn(teacherDetails);
    given(teacherDetailsModelAssembler.toModel(any(TeacherDetails.class))).willCallRealMethod();

    // Perform request
    mvc.perform(
            get("/api/users/1/teacher-details")
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/hal+json"))
        // Assert response
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
  public void getTeacherDetailsThrowsProperException() throws Exception {
    //  Mock service to throw exception
    willThrow(new EntityNotFoundException(ErrorMessages.teacherDetailsNotFound(1L)))
        .given(userService)
        .getTeacherDetails(any(Long.class));

    // Perform request
    mvc.perform(
            get("/api/users/1/teacher-details")
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/hal+json"))
        // Assert response
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(ErrorMessages.teacherDetailsNotFound(1L)))
        .andExpect(jsonPath("$.path").value("/api/users/1/teacher-details"))
        .andExpect(jsonPath("$.statusCode").value(404));
  }

  @Test
  public void updateTeacherDetailsWithProperRequest() throws Exception {
    // Prepare test data
    User teacher = Mockito.mock(User.class);
    given(teacher.getId()).willReturn(1L);
    PatchTeacherDetailsRequest request =
        PatchTeacherDetailsRequest.builder()
            .bio("New bio")
            .title(DegreeTitle.DOCTOR)
            .tutorship("New tutorship")
            .degreeField("New degree field")
            .build();
    TeacherDetails modifiedTeacherDetails =
        TeacherDetails.builder()
            .teacher(teacher)
            .id(1L)
            .bio(request.getBio())
            .title(request.getTitle())
            .tutorship(request.getTutorship())
            .degreeField(request.getDegreeField())
            .build();

    // Mocks
    given(userService.updateTeacherDetails(any(PatchTeacherDetailsRequest.class), any(Long.class)))
        .willReturn(modifiedTeacherDetails);
    given(teacherDetailsModelAssembler.toModel(any(TeacherDetails.class))).willCallRealMethod();

    // Perform request
    mvc.perform(
            patch("/api/users/1/teacher-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept("application/hal+json"))
        // Assert response
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bio").value(request.getBio()))
        .andExpect(jsonPath("$.tutorship").value(request.getTutorship()))
        .andExpect(jsonPath("$.title").value(request.getTitle().toString()))
        .andExpect(jsonPath("$.degreeField").value(request.getDegreeField()))
        .andExpect(jsonPath("$.links[*].rel", Matchers.hasItems("self", "teacher")))
        .andExpect(jsonPath("$.links[*].href", Matchers.hasSize(2)))
        .andReturn();
  }

  @Test
  public void updateTeacherDetails_ThrowsEntityNotFoundException() {
    // Prepare test data
    PatchTeacherDetailsRequest request =
        PatchTeacherDetailsRequest.builder()
            .bio("New bio")
            .title(DegreeTitle.DOCTOR)
            .tutorship("New tutorship")
            .degreeField("New degree field")
            .build();

    // Mocks
    given(userService.updateTeacherDetails(any(PatchTeacherDetailsRequest.class), any(Long.class)))
        .willThrow(new EntityNotFoundException(ErrorMessages.teacherDetailsNotFound(1L)));

    // Perform request
    assertThrows(
        EntityNotFoundException.class,
        () -> userController.patchTeacherDetails(request, 1L),
        ErrorMessages.teacherDetailsNotFound(1L));
  }
}
