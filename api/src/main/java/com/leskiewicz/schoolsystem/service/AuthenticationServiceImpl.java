package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.assembler.UserModelAssembler;
import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.dto.request.AuthenticationRequest;
import com.leskiewicz.schoolsystem.dto.request.RegisterRequest;
import com.leskiewicz.schoolsystem.dto.response.AuthenticationResponse;
import com.leskiewicz.schoolsystem.model.Degree;
import com.leskiewicz.schoolsystem.model.Faculty;
import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.model.enums.Role;
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

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final FacultyService facultyService;
    private final DegreeService degreeService;
    private final UserModelAssembler userModelAssembler;

    public AuthenticationResponse register(RegisterRequest request) {
        Faculty faculty = facultyService.getByName(request.getFacultyName());
        Degree degree = degreeService.getByTitleAndFieldOfStudy(request.getDegreeTitle(), request.getDegreeField());

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_STUDENT)
                .degree(degree)
                .faculty(faculty)
                .build();
        userService.addUser(user);
        var jwtToken = jwtUtils.generateToken(user);
        UserDto userDto = userModelAssembler.toModel(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(userDto)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userService.getByEmail(request.getEmail());
        var jwtToken = jwtUtils.generateToken(user);
        UserDto userDto = userModelAssembler.toModel(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(userDto)
                .build();
    }
}
