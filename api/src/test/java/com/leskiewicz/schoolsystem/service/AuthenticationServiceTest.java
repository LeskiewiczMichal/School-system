package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.security.AuthenticationServiceImpl;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.security.utils.JwtUtils;
import com.leskiewicz.schoolsystem.security.utils.JwtUtilsImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    // Mocks
    @Mock
    private UserRepository userRepository;
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
    private LinksService linksService;

    @InjectMocks
    AuthenticationServiceImpl authenticationService;

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

        newUser = User.builder()
                .email("johndoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("encoded_password")
                .faculty(faculty)
                .role(Role.ROLE_STUDENT)
                .degree(degree)
                .build();
    }

    @Test
    public void registerHappyPath() {
        given(facultyService.getByName("Engineering")).willReturn(faculty);
        given(passwordEncoder.encode("12345")).willReturn("encoded_password");
        given(jwtUtils.generateToken(newUser)).willReturn("12");
        given(degreeService.getByTitleAndFieldOfStudy(DegreeTitle.BACHELOR, "Computer Science")).willReturn(degree);

        AuthenticationResponse authenticationResponse = authenticationService.register(request);

        // Proper response
        Assertions.assertEquals(newUser, authenticationResponse.getUser());
        Assertions.assertEquals("12", authenticationResponse.getToken());

        // User was saved in repository
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertEquals(newUser, savedUser);

        // Links where added
        verify(linksService).addLinks(newUser);
    }

    @Test
    public void registerThrowsExceptionOnFacultyNotFound() {
        given(facultyService.getByName("Engineering"))
                .willReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            authenticationService.register(request);
        });
    }

    @Test
    public void registerThrowsExceptionOnDegreeNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                authenticationService.register(request));
    }

    @Test
    public void authenticateHappyPath() {
        AuthenticationRequest request = new AuthenticationRequest("johndoe@example.com", "password");

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>()));
        given(userRepository.findByEmail("johndoe@example.com")).willReturn(Optional.of(newUser));
        given(jwtUtils.generateToken(any(User.class))).willReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        // Proper response
        Assertions.assertEquals("jwtToken", response.getToken());
        Assertions.assertEquals(newUser, response.getUser());

        // Links where added
        verify(linksService).addLinks(newUser);
    }

    @Test
    public void authenticateUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("johndoe@example.com", "password");

        given(userRepository.findByEmail("johndoe@example.com")).willReturn(Optional.empty());

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
