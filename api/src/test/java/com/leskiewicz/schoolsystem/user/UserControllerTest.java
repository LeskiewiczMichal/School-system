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
  public void getUserByIdReturnsCorrectUser() {
    // Mock input and output data
    Faculty faculty = Mockito.mock(Faculty.class);
    Degree degree = Mockito.mock(Degree.class);
    given(faculty.getName()).willReturn("Test");
    given(degree.getFieldOfStudy()).willReturn("Law");
    UserDto userDto = TestHelper.createUserDto(faculty, degree);

    // Mock service
    given(userService.getById(userDto.getId())).willReturn(userDto);

    // Mock assembler
    given(userDtoAssembler.toModel(userDto)).willReturn(userDto);

    // Call controller
    ResponseEntity<UserDto> result = userController.getUserById(userDto.getId());

    // Verify result
    Assertions.assertEquals(userDto, result.getBody());
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    // Verify mocks
    verify(userService, times(1)).getById(userDto.getId());
    verify(userDtoAssembler, times(1)).toModel(userDto);
  }

//  @Test
//  public void getUsers() {
//    PageableRequest request = new PageableRequest();
//
//    // Mock the list of userDto
//    List<UserDto> userDtoList =
//        Arrays.asList(Mockito.mock(UserDto.class), Mockito.mock(UserDto.class));
//
//    // Mock the page
//    Page<UserDto> userDtoPage = new PageImpl<>(userDtoList);
//
//    // Mock service
//    given(userService.getUsers(request.toPageable())).willReturn(userDtoPage);
//
//    // Mock assembler
//    given(userDtoAssembler.toModel(any(UserDto.class)))
//        .willReturn(userDtoList.get(0), userDtoList.get(1));
//
//    // Mock paged resources assembler
//    PagedModel<EntityModel<UserDto>> pagedModel = Mockito.mock(PagedModel.class);
//    given(userPagedResourcesAssembler.toModel(any(Page.class))).willReturn(pagedModel);
//
//    // Call controller
//    ResponseEntity<RepresentationModel<UserDto>> response = userController.getUsers(request);
//
//    // Assert the response
//    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//    Assertions.assertEquals(HalModelBuilder.halModelOf(pagedModel).build(), response.getBody());
//
//    // Verify mocks
//    verify(userService, times(1)).getUsers(any(Pageable.class));
//    verify(userDtoAssembler, times(2)).toModel(any(UserDto.class));
//    verify(userPagedResourcesAssembler, times(1)).toModel(userDtoPage);
//  }

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
  public void testPathUser() {
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

//  @Test
//  public void getUserCourses() {
//    PageableRequest request = new PageableRequest();
//
//    // Mock the list of userDto
//    List<CourseDto> userDtoList =
//        Arrays.asList(Mockito.mock(CourseDto.class), Mockito.mock(CourseDto.class));
//
//    // Mock the page
//    Page<CourseDto> userDtoPage = new PageImpl<>(userDtoList);
//
//    // Mock service
//    given(userService.getUserCourses(1L, request.toPageable())).willReturn(userDtoPage);
//
//    // Mock assembler
//    given(courseDtoAssembler.toModel(any(CourseDto.class)))
//        .willReturn(userDtoList.get(0), userDtoList.get(1));
//
//    // Mock paged resources assembler
//    PagedModel<EntityModel<CourseDto>> pagedModel = Mockito.mock(PagedModel.class);
//    given(coursePagedResourcesAssembler.toModel(userDtoPage)).willReturn(pagedModel);
//
//    // Call controller
//    ResponseEntity<RepresentationModel<CourseDto>> response =
//        userController.getUserCourses(1L, request);
//
//    // Assert the response
//    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//    Assertions.assertEquals(HalModelBuilder.halModelOf(pagedModel).build(), response.getBody());
//
//    // Verify mocks
//    verify(userService, times(1)).getUserCourses(any(Long.class), any(Pageable.class));
//    verify(courseDtoAssembler, times(2)).toModel(any(CourseDto.class));
//    verify(coursePagedResourcesAssembler, times(1)).toModel(userDtoPage);
//  }

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
