package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.dto.request.AuthenticationRequest;
import com.leskiewicz.schoolsystem.dto.request.RegisterRequest;
import com.leskiewicz.schoolsystem.dto.response.AuthenticationResponse;
import com.leskiewicz.schoolsystem.model.Faculty;
import com.leskiewicz.schoolsystem.model.Role;
import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.repository.UserRepository;
import com.leskiewicz.schoolsystem.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final FacultyService facultyService;

    public AuthenticationResponse register(RegisterRequest request) {
        Faculty faculty = facultyService.getByName(request.getFacultyName());
        if (faculty == null) {
            throw new EntityNotFoundException("Faculty with given name not found");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .faculty(faculty)
                .build();
        userRepository.save(user);
        var jwtToken = jwtUtils.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtUtils.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }
}
