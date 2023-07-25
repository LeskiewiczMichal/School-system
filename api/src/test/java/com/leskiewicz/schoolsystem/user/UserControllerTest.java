package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  // Services
  private UserService userService;

  // Assemblers
  private UserDtoAssembler userDtoAssembler;
  private PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;
  private CourseDtoAssembler courseDtoAssembler;
  private PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  private UserController userController;

  // Annotation mocks didn't work as expected here, pagedResourcesAssemblers were mixed up
  @BeforeEach
  public void setUp() {
    // Create mock instances
    userService = Mockito.mock(UserService.class);
    userDtoAssembler = Mockito.mock(UserDtoAssembler.class);
    userPagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);

    // Create UserController instance
    userController =
        new UserController(
            userService,
            userDtoAssembler,
            courseDtoAssembler,
            userPagedResourcesAssembler,
            coursePagedResourcesAssembler);
  }

  @Test
  public void getUserById() {
    //    // Mock input and output data
    Faculty faculty = Mockito.mock(Faculty.class);
    Degree degree = Mockito.mock(Degree.class);
    given(faculty.getName()).willReturn("Test");
    given(degree.getFieldOfStudy()).willReturn("Law");
    UserDto userDto = TestHelper.createUserDto(faculty, degree);

    CommonTests.controllerGetEntitiyById(
            userDto,
            1L,
            userService::getById,
            userDtoAssembler::toModel,
            userController::getUserById);

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
    // Prepare data
    Long userId = 1L;
    PatchUserRequest request = Mockito.mock(PatchUserRequest.class);
    UserDto existingUserDto = Mockito.mock(UserDto.class);

    // Mock service
    given(userService.updateUser(request, userId)).willReturn(existingUserDto);

    // Mock assembler
    given(userDtoAssembler.toModel(any(UserDto.class))).willReturn(existingUserDto);

    // Call controller
    ResponseEntity<UserDto> response = userController.patchUser(request, userId);

    // Verify response
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(existingUserDto, response.getBody());
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
}
