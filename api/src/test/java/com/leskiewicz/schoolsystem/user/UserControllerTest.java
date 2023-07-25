package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  @Mock private UserService userService;
  @Mock private UserDtoAssembler userDtoAssembler;
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
}
