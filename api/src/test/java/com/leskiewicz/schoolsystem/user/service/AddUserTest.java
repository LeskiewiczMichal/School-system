package com.leskiewicz.schoolsystem.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.error.customexception.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddUserTest {

  // Variables
  Faculty faculty;
  Degree degree;
  User user;

  // Mocks
  @Mock private UserRepository userRepository;
  @InjectMocks private UserServiceImpl userService;

  @BeforeEach
  public void setUp() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);
    user = TestHelper.createUser(faculty, degree);
  }

  @Test
  public void addUserSavesUser() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

    userService.addUser(user);

    verify(userRepository).save(user);
  }

  @Test
  public void throwsUserAlreadyExistsExceptionWhenUserWithGivenEmailAlreadyExists() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

    Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(user));
  }
}
