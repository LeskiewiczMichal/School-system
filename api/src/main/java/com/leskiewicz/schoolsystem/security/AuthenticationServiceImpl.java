package com.leskiewicz.schoolsystem.security;

import com.leskiewicz.schoolsystem.security.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.security.utils.JwtUtils;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.user.utils.UserModelAssembler;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

        User user = userService.getByEmail(request.getEmail());
        var jwtToken = jwtUtils.generateToken(user);
        UserDto userDto = userModelAssembler.toModel(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(userDto)
                .build();
    }
}
