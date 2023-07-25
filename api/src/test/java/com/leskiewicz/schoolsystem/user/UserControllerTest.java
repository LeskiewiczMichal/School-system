package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  // Services
  @Mock private UserService userService;

  // Assemblers
  @Mock private UserDtoAssembler userDtoAssembler;
  @Mock private PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;

  @InjectMocks private UserController userController;

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
  //    //    Faculty faculty = Mockito.mock(Faculty.class);
  //    //    Degree degree = Mockito.mock(Degree.class);
  //    //    given(faculty.getName()).willReturn("Test");
  //    //    given(degree.getFieldOfStudy()).willReturn("Law");
  //
  //    // Mock the list of userDto
  //    List<UserDto> userDtoList =
  //        Arrays.asList(Mockito.mock(UserDto.class), Mockito.mock(UserDto.class));
  //
  //    // Mock the page
  //    Page<UserDto> userDtoPage = Mockito.mock(Page.class);
  //
  //    // Mock service
  //    given(userService.getUsers(request.toPageable())).willReturn(userDtoPage);
  //
  //    // Mock assembler
  //    given(userDtoPage.map(userDtoAssembler::toModel)).willReturn(userDtoPage);
  //
  //    // Mock paged resources assembler
  //    PagedModel<RepresentationModel<UserDto>> pagedModel;
  //    given(userPagedResourcesAssembler.toModel(userDtoPage)).willReturn(userDtoPage);
  //
  //  }

  @Test
  public void getUsers() {
    PageableRequest request = new PageableRequest();

    // Mock the list of userDto
    List<UserDto> userDtoList =
        Arrays.asList(Mockito.mock(UserDto.class), Mockito.mock(UserDto.class));

    // Mock the page
    Page<UserDto> userDtoPage = new PageImpl<>(userDtoList);

    // Mock service
    given(userService.getUsers(request.toPageable())).willReturn(userDtoPage);

    // Mock assembler
    given(userDtoAssembler.toModel(any(UserDto.class)))
        .willReturn(userDtoList.get(0), userDtoList.get(1));

    // Mock paged resources assembler
    PagedModel<EntityModel<UserDto>> pagedModel = Mockito.mock(PagedModel.class);
    given(userPagedResourcesAssembler.toModel(userDtoPage)).willReturn(pagedModel);

    // Call controller
    ResponseEntity<RepresentationModel<UserDto>> response = userController.getUsers(request);

    // Assert the response
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(HalModelBuilder.halModelOf(pagedModel).build(), response.getBody());

    // Verify mocks
    verify(userService, times(1)).getUsers(any(Pageable.class));
    verify(userDtoAssembler, times(2)).toModel(any(UserDto.class));
    verify(userPagedResourcesAssembler, times(1)).toModel(userDtoPage);
  }
}
