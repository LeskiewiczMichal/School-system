package com.leskiewicz.schoolsystem.security;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.security.utils.JwtUtils;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserService;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserService userService;
  private final FacultyService facultyService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;
  private final AuthenticationManager authenticationManager;
  private final UserModelAssembler userModelAssembler;

  public AuthenticationResponse register(RegisterRequest request) {
    // Retrieve faculty and degree
    Faculty faculty = facultyService.getByName(request.getFacultyName());
    Degree degree = facultyService.getDegreeByTitleAndFieldOfStudy(faculty,
        request.getDegreeTitle(), request.getDegreeField());

    User user = new User(
        null,
        request.getFirstName(),
        request.getLastName(),
        request.getEmail(),
        passwordEncoder.encode(request.getPassword()),
        faculty,
        degree,
        Role.ROLE_STUDENT
    );

    // Save new user and generate jwt token
    userService.addUser(user);
    var jwtToken = jwtUtils.generateToken(user);
    UserDto userDto = userModelAssembler.toModel(user);

    return new AuthenticationResponse(jwtToken, userDto);
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

    return new AuthenticationResponse(jwtToken, userDto);
  }
}
