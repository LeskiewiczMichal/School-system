package com.leskiewicz.schoolsystem.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
// import static
// org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leskiewicz.schoolsystem.authentication.SecurityService;
import com.leskiewicz.schoolsystem.authentication.SecurityServiceImpl;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.error.DefaultExceptionHandler;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

  private SecurityService securityService;
  // Used to convert DTOs to HAL representations
  private CourseDtoAssembler courseDtoAssembler;
  private UserDtoAssembler userDtoAssembler;

  // Used to add links to paged resources
  private PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;
  private PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;

  private CourseService courseService;

  private CourseController courseController;

  private MockMvc mvc;

  @BeforeEach
  public void setup() {
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    courseService = Mockito.mock(CourseService.class);
    userDtoAssembler = Mockito.mock(UserDtoAssembler.class);
    userPagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    securityService = Mockito.mock(SecurityServiceImpl.class);

    courseController =
        new CourseController(
            courseService,
            securityService,
            courseDtoAssembler,
            userDtoAssembler,
            coursePagedResourcesAssembler,
            userPagedResourcesAssembler);

    mvc =
        MockMvcBuilders.standaloneSetup(courseController)
            .setControllerAdvice(new DefaultExceptionHandler())
            .build();
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

  @Test
  public void createCourse() {
    CourseDto courseDto = TestHelper.createCourseDto("faculty", "teacher name");
    CreateCourseRequest createCourseRequest =
        CreateCourseRequest.builder()
            .title("Course Title")
            .facultyId(1L)
            .teacherId(1L)
            .durationInHours(10)
            .build();

    CommonTests.controllerCreateEntity(
        courseDto,
        CourseDto.class,
        courseDto.add(WebMvcLinkBuilder.linkTo(CourseController.class).withSelfRel()),
        CreateCourseRequest.class,
        createCourseRequest,
        courseService::createCourse,
        courseDtoAssembler::toModel,
        courseController::createCourse);
  }

  @Test
  public void getCourseStudents() {
    CommonTests.controllerGetEntities(
        UserDto.class,
        userPagedResourcesAssembler,
        (pageable) -> courseService.getCourseStudents(1L, pageable),
        userDtoAssembler::toModel,
        (pageable) -> courseController.getCourseStudents(1L, pageable));
  }

  @Test
  public void addStudentToCourse() throws Exception {
    mvc.perform(
            post("/api/courses/1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1")
                .accept("application/hal+json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Student added to course successfully"))
        .andExpect(jsonPath("$.links[*].rel", Matchers.hasItems("student", "course")))
        .andExpect(jsonPath("$.links[*].href", Matchers.hasSize(2)))
        .andReturn();
  }

  @Test
  public void addStudentToCourseThrowsProperException() throws Exception {
    willThrow(new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Course", 1L)))
        .given(courseService)
        .addStudentToCourse(any(Long.class), any(Long.class));

    mvc.perform(
            post("/api/courses/1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1")
                .accept("application/hal+json"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(ErrorMessages.objectWithIdNotFound("Course", 1L)))
        .andExpect(jsonPath("$.path").value("/api/courses/1/students"))
        .andReturn();
  }

  @Test
  public void uploadFile() throws Exception {
    // Mock service method to do nothing
    doNothing().when(courseService).storeFile(any(MultipartFile.class), any(Long.class));

    // Create file for testing
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    // Perform request and assert result
    mvc.perform(
            multipart("/api/courses/1/files")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/hal+json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("File with name: hello.txt uploaded successfully"))
        .andExpect(jsonPath("$.links[*].rel", Matchers.hasItems("course")))
        .andExpect(jsonPath("$.links[*].href", Matchers.hasSize(1)))
        .andReturn();
  }

  @Test
  public void uploadFileThrowsProperException() throws Exception {
    // Mock service method to throw exception
    willThrow(new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Course", 1L)))
        .given(courseService)
        .storeFile(any(MultipartFile.class), any(Long.class));

    // Create file for testing
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    // Perform request and assert result
    mvc.perform(
            multipart("/api/courses/1/files")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/hal+json"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(ErrorMessages.objectWithIdNotFound("Course", 1L)))
        .andExpect(jsonPath("$.path").value("/api/courses/1/files"))
        .andReturn();
  }

  @Test
  public void getCourseFilesTest() throws Exception {
    // Prepare test data
    List<File> files = Arrays.asList(Mockito.mock(File.class), Mockito.mock(File.class));
    Page<File> filesPage = new PageImpl<>(files);

    // Mocks
    given(courseService.getCourseFiles(any(Long.class), any(Pageable.class))).willReturn(filesPage);

    // Call endpoint and assert result
  }
}
