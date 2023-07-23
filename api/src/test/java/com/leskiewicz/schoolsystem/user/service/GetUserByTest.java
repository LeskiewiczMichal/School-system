package com.leskiewicz.schoolsystem.user.service;

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
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetUserByTest {

  // Variables
  Faculty faculty;
  Degree degree;
  User user;

  // Mocks
  @Mock private UserRepository userRepository;
  @Mock private DegreeService degreeService;
  @Mock private FacultyService facultyService;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserMapper userMapper;
  @InjectMocks private UserServiceImpl userService;

  @BeforeEach
  public void setUp() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);
    user = TestHelper.createUser(faculty, degree);
  }

  //*** GetById ***//

  @Test
  public void getByIdHappyPath() {
    UserDto dto = Mockito.mock(UserDto.class);
    given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
    given(userMapper.convertToDto(user)).willReturn(dto);

    UserDto testUser = userService.getById(1L);

    Assertions.assertEquals(dto, testUser);
  }

  @Test
  public void getByIdThrowsEntityNotFound() {
    given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));
  }

  //*** GetByEmail ***//

  @Test
  public void getByEmailHappyPath() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

    User testUser = userService.getByEmail("email@example.com");

    Assertions.assertEquals(user, testUser);
  }

  @Test
  public void getByEmailThrowsEntityNotFound() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class,
            () -> userService.getByEmail("email@example/com"));
  }

}
