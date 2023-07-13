package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.dto.request.AuthenticationRequest;
import com.leskiewicz.schoolsystem.dto.request.RegisterRequest;
import com.leskiewicz.schoolsystem.dto.response.AuthenticationResponse;
import com.leskiewicz.schoolsystem.model.Faculty;
import com.leskiewicz.schoolsystem.model.enums.Role;
import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.repository.UserRepository;
import com.leskiewicz.schoolsystem.utils.JwtUtils;
import com.leskiewicz.schoolsystem.utils.JwtUtilsImpl;
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

    @InjectMocks AuthenticationServiceImpl authenticationService;

    // Variables
    private RegisterRequest request;
    private Faculty faculty;
    private User newUser;

    @BeforeEach
    public void setUp() {
//        Set up test data
        faculty = new Faculty();
        faculty.setName("Engineering");

        request = RegisterRequest.builder()
                .email("johndoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("12345")
                .facultyName("Engineering")
                .build();

        newUser = User.builder()
                .email("johndoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("encoded_password")
                .faculty(faculty)
                .role(Role.STUDENT)
                .build();
    }

    @Test
    public void registerHappyPath() {
        given(facultyService.getByName("Engineering")).willReturn(faculty);
        given(passwordEncoder.encode("12345")).willReturn("encoded_password");
        given(jwtUtils.generateToken(newUser)).willReturn("12");

        AuthenticationResponse authenticationResponse = authenticationService.register(request);

        Assertions.assertEquals(newUser, authenticationResponse.getUser()); // Proper user response
        Assertions.assertEquals("12", authenticationResponse.getToken()); // Proper token response
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture()); // User saved in repository
        User savedUser = userCaptor.getValue();
        Assertions.assertEquals(newUser, savedUser); // Saved proper user
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
    public void authenticateHappyPath() {
        AuthenticationRequest request = new AuthenticationRequest("johndoe@example.com", "password");

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>()));
        given(userRepository.findByEmail("johndoe@example.com")).willReturn(Optional.of(new User()));
        given(jwtUtils.generateToken(any(User.class))).willReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        Assertions.assertEquals("jwtToken", response.getToken());
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
