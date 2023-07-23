package com.leskiewicz.schoolsystem.user.service;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetUsersTest {

  // Mocks
  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @InjectMocks private UserServiceImpl userService;

  @Test
  public void getUsersReturnsPagedUsers() {
    Faculty faculty = Mockito.mock(Faculty.class);
    Degree degree = Mockito.mock(Degree.class);
    List<User> userList = Arrays.asList(
            new User(1L, "John", "Doe", "john.doe@example.com", "12345", faculty, degree, Role.ROLE_STUDENT),
            new User(2L, "Jane", "Smith", "jane.smith@example.com", "12345", faculty, degree, Role.ROLE_ADMIN)
    );
    Page<User> usersPage = new PageImpl<>(userList);

    given(userRepository.findAll(any(Pageable.class))).willReturn(usersPage);

    // Mock the behavior of the userMapper
    UserDto userDto1 = new UserDto(1L, "John", "Doe", "john.doe@example.com", "Some Faculty", "Some Degree", 1L);
    UserDto userDto2 = new UserDto(2L, "Jane", "Smith", "jane.smith@example.com", "Another Faculty", "Another Degree", 2L);
    given(userMapper.convertToDto(any(User.class))).willReturn(userDto1, userDto2);

    // Call the method to test
    Page<UserDto> result = userService.getUsers(PageRequest.of(0, 10));

    // Assert the result
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(userDto1, result.getContent().get(0));
    Assertions.assertEquals(userDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(userRepository, times(1)).findAll(any(Pageable.class));
    verify(userMapper, times(2)).convertToDto(any(User.class));
  }
}
