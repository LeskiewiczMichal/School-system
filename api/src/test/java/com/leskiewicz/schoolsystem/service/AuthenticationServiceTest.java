package com.leskiewicz.schoolsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.authentication.AuthenticationServiceImpl;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.authentication.utils.JwtUtils;
import com.leskiewicz.schoolsystem.authentication.utils.JwtUtilsImpl;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserService;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationServiceImpl authenticationService;
    //region Mocks
    @Mock
    private UserService userService;
    @Mock
    private FacultyService facultyService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils = new JwtUtilsImpl();
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private DegreeService degreeService;
    @Mock
    private UserMapper userMapper;
    //endregion

    // Variables
    private RegisterRequest request;
    private Faculty faculty;
    private User newUser;
    private Degree degree;

    @BeforeEach
    public void setUp() {
//        Set up test data
        faculty = new Faculty();
        faculty.setName("Engineering");

        degree = Degree.builder()
                .title(DegreeTitle.BACHELOR)
                .fieldOfStudy("Computer Science")
                .faculty(faculty)
                .build();

        request = RegisterRequest.builder()
                .email("johndoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("12345")
                .facultyName("Engineering")
                .degreeField("Computer Science")
                .degreeTitle(DegreeTitle.BACHELOR)
                .build();

        newUser = new User(
                null,
                "John",
                "Doe",
                "johndoe@example.com",
                "encoded_password",
                faculty,
                degree,
                Role.ROLE_STUDENT
        );
    }

    //region Register Tests
    @Test
    public void registerHappyPath() {
        given(facultyService.getByName("Engineering")).willReturn(faculty);
        given(passwordEncoder.encode("12345")).willReturn("encoded_password");
        given(jwtUtils.generateToken(newUser)).willReturn("12");
        given(facultyService.getDegreeByTitleAndFieldOfStudy(faculty, request.getDegreeTitle(), request.getDegreeField())).willReturn(degree);
        UserDto mockUserDto = Mockito.mock(UserDto.class);
        given(userMapper.convertToDto(newUser)).willReturn(mockUserDto);

        AuthenticationResponse authenticationResponse = authenticationService.register(request);

        // Proper response
        Assertions.assertEquals(mockUserDto, authenticationResponse.getUser());
        Assertions.assertEquals("12", authenticationResponse.getToken());

        // User was saved in repository
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).addUser(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertEquals(newUser, savedUser);

        // User was converted into dto
    }

    @Test
    public void registerThrowsExceptionOnFacultyNotFound() {
        given(facultyService.getByName(any(String.class)))
                .willThrow(new EntityNotFoundException());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            authenticationService.register(request);
        });
    }

    @Test
    public void registerThrowsExceptionOnDegreeNotInFaculty() {
        given(facultyService.getByName(any())).willReturn(faculty);
        doThrow(new IllegalArgumentException()).when(facultyService).getDegreeByTitleAndFieldOfStudy(faculty, degree.getTitle(), degree.getFieldOfStudy());

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                authenticationService.register(request));
    }
    //endregion

    //region Authentication Tests
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
    //endregion
}
