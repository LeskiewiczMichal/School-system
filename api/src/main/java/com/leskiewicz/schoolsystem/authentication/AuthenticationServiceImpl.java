package com.leskiewicz.schoolsystem.authentication;

import com.leskiewicz.schoolsystem.authentication.dto.RegisterTeacherRequest;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.authentication.utils.JwtUtils;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsRepository;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserService;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.Mapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
  private final FacultyRepository facultyRepository;
  private final TeacherDetailsRepository teacherDetailsRepository;
  private final Mapper<User, UserDto> userMapper;

  public AuthenticationResponse register(RegisterRequest request) {
    // Retrieve faculty and degree
    Faculty faculty = facultyService.getByName(request.getFacultyName());
    Degree degree =
        facultyService.getDegreeByTitleAndFieldOfStudy(
            faculty, request.getDegreeTitle(), request.getDegreeField());

    User user =
        new User(
            null,
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            faculty,
            degree,
            null,
            Role.ROLE_STUDENT,
            null);

    // Save new user and generate jwt token
    userService.addUser(user);
    var jwtToken = jwtUtils.generateToken(user);
    UserDto userDto = userMapper.mapToDto(user);

    return new AuthenticationResponse(jwtToken, userDto);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // Authenticate user and generate jwt token
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    User user = userService.getByEmail(request.getEmail());
    var jwtToken = jwtUtils.generateToken(user);

    // Convert user to dto and create response
    UserDto userDto = userMapper.mapToDto(user);
    return new AuthenticationResponse(jwtToken, userDto);
  }

  public AuthenticationResponse registerTeacherAccount(RegisterTeacherRequest request) {
    // Find necessary entities
    Faculty faculty =
        facultyRepository
            .findById(request.getFaculty())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("Faculty", request.getFaculty())));

    TeacherDetails teacherDetails =
            TeacherDetails.builder()
                    .teacher(null)
                    .degreeField(request.getDegreeField())
                    .title(request.getTitle())
                    .bio("")
                    .tutorship("")
                    .build();

    // Create new user and teacher details
    User user =
        new User(
            null,
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            faculty,
            null,
                teacherDetails,
            Role.ROLE_TEACHER,
            null);


    teacherDetails.setTeacher(user);

    // Save new user, teacher details and generate jwt token
    userService.addUser(user);
    teacherDetailsRepository.save(teacherDetails);
    var jwtToken = jwtUtils.generateToken(user);

    // Convert user to dto and create response
    UserDto userDto = userMapper.mapToDto(user);
    return new AuthenticationResponse(jwtToken, userDto);
  }

  @Override
  public AuthenticationResponse authenticateWithToken(UserDetails userDetails) {
    if (userDetails == null) {
      throw new EntityNotFoundException("User not found");
    }

    User user = userService.getByEmail(userDetails.getUsername());
    var jwtToken = jwtUtils.generateToken(user);

    // Convert user to dto and create response
    UserDto userDto = userMapper.mapToDto(user);
    return new AuthenticationResponse(jwtToken, userDto);
  }
}
