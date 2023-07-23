package com.leskiewicz.schoolsystem.authentication.service;

import com.leskiewicz.schoolsystem.authentication.AuthenticationServiceImpl;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.authentication.utils.JwtUtils;
import com.leskiewicz.schoolsystem.authentication.utils.JwtUtilsImpl;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserService;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.mysql.cj.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthenticateTest {

  @InjectMocks AuthenticationServiceImpl authenticationService;
  @Mock private UserService userService;
  @Mock private JwtUtils jwtUtils = new JwtUtilsImpl();
  @Mock private AuthenticationManager authenticationManager;
  @Mock private UserMapper userMapper;

  // Variables
  private Faculty faculty;
  private User newUser;
  private Degree degree;

  @BeforeEach
  public void setUp() {
//        Set up test data
    faculty = TestHelper.createFaculty();
    degree = TestHelper.createDegree(faculty);
    newUser = TestHelper.createUser(faculty, degree);
  }


  @Test
  public void authenticateHappyPath() {
    AuthenticationRequest request = new AuthenticationRequest(newUser.getEmail(), "password");

    given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willReturn(new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>()));
    given(userService.getByEmail(newUser.getEmail())).willReturn(newUser);
    given(jwtUtils.generateToken(any(User.class))).willReturn("jwtToken");

    UserDto mockUserDto = UserDto.builder()
            .id(8L)
            .firstName(newUser.getFirstName())
            .lastName(newUser.getLastName())
            .faculty(newUser.getFaculty().getName())
            .degree(newUser.getDegree().getFieldOfStudy())
            .email(newUser.getEmail())
            .facultyId(10L)
            .build();
    given(userMapper.convertToDto(any(User.class))).willReturn(mockUserDto);

    AuthenticationResponse response = authenticationService.authenticate(request);

    mockUserDto = UserDto.builder()
            .id(response.getUser().getId())
            .firstName(newUser.getFirstName())
            .lastName(newUser.getLastName())
            .faculty(newUser.getFaculty().getName())
            .degree(newUser.getDegree().getFieldOfStudy())
            .email(newUser.getEmail())
            .facultyId(10L)
            .build();
    // Proper response
    Assertions.assertEquals("jwtToken", response.getToken());
    Assertions.assertEquals(mockUserDto, response.getUser());
  }

  @Test
  public void authenticateUserNotFound() {
    AuthenticationRequest request = new AuthenticationRequest("johndoe@example.com", "password");

    given(userService.getByEmail("johndoe@example.com")).willThrow(new UsernameNotFoundException("User with given email not found"));

    Assertions.assertThrows(UsernameNotFoundException.class, () ->
            authenticationService.authenticate(request));
  }

  @Test
  public void authenticateWrongCredentials() {
    AuthenticationRequest request = new AuthenticationRequest("johndoe@example.com", "wrong_password");

    given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willThrow(new BadCredentialsException("Bad credentials"));

    Assertions.assertThrows(BadCredentialsException.class, () ->
            authenticationService.authenticate(request));
  }
}
