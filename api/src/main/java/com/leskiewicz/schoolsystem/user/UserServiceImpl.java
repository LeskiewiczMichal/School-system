package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.MissingFieldException;
import com.leskiewicz.schoolsystem.error.customexception.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.user.teacherdetails.PatchTeacherDetailsRequest;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsRepository;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.Mapper;
import com.leskiewicz.schoolsystem.utils.Support;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final FileService fileService;
  private final PasswordEncoder passwordEncoder;
  private final Support support;

  // Repositories
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final TeacherDetailsRepository teacherDetailsRepository;

  // Mappers
  private final Mapper<User, UserDto> userMapper;
  private final CourseMapper courseMapper;

  @Override
  public UserDto getById(Long id) {
    return userMapper.mapToDto(retrieveUserFromRepository(id));
  }

  @Override
  public User getByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () ->
                new EntityNotFoundException(ErrorMessages.objectWithEmailNotFound("User", email)));
  }

  @Override
  public Page<UserDto> getUsers(Pageable pageable) {
    Page<User> users = userRepository.findAll(pageable);
    return userMapper.mapPageToDto(users);
  }

  @Override
  public void addUser(User user) {
    ValidationUtils.validate(user);
    checkIfUserWithEmailAlreadyExists(user.getEmail());
    User savedUser = userRepository.save(user);
    support.notifyCreated("User", savedUser.getId());
  }


  @Override
  public UserDto updateUser(PatchUserRequest request, Long userId) {
    checkIfUserWithEmailAlreadyExists(request.email());
    User user = retrieveUserFromRepository(userId);
    user.update(request, passwordEncoder);
    ValidationUtils.validate(user);
    userRepository.save(user);
    support.notifyUpdated("User", user.getId());
    return userMapper.mapToDto(user);
  }

  @Override
  public Faculty getUserFaculty(Long userId) {
    return userRepository
        .findFacultyByUserId(userId)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "User with ID: " + userId + " does not have associated faculty"));
  }

  @Override
  public Degree getUserDegree(Long userId) {
    return userRepository
        .findDegreeByUserId(userId)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "User with ID: " + userId + " does not have associated degree"));
  }

  @Override
  public Page<CourseDto> getUserCourses(Long userId, Pageable pageable) {
    User user = retrieveUserFromRepository(userId);
    Page<Course> courses = retrieveUserCourses(user, pageable);
    return courseMapper.mapPageToDto(courses);
  }

  @Override
  public TeacherDetails getTeacherDetails(Long userId) {
    userExistsCheck(userId);
    return retrieveTeacherDetailsFromRepository(userId);
  }

  @Override
  public TeacherDetails updateTeacherDetails(PatchTeacherDetailsRequest request, Long userId) {
    TeacherDetails teacherDetails = retrieveTeacherDetailsFromRepository(userId);
    teacherDetails.update(request);
    ValidationUtils.validate(teacherDetails);
    teacherDetailsRepository.save(teacherDetails);
    support.notifyUpdated("TeacherDetails", teacherDetails.getId());
    return teacherDetails;
  }

  @Override
  public Page<UserDto> search(String lastName, String firstName, Role role, Pageable pageable) {
    Page<User> users =
        userRepository.searchUsersByLastNameAndFirstNameAndRole(
            lastName, firstName, role, pageable);
    return userMapper.mapPageToDto(users);
  }

  @Override
  public void addImage(Long userId, MultipartFile image) {
    User user = retrieveUserFromRepository(userId);
    String filename = fileService.uploadImage(image);
    user.setProfilePictureName(filename);
    userRepository.save(user);
  }

  private void checkIfUserWithEmailAlreadyExists(String email) {
    if (email == null) {
      return;
    }
    if (userWithEmailAlreadyExists(email)) {
      throw new UserAlreadyExistsException(ErrorMessages.userWithEmailAlreadyExists(email));
    }
  }

  private void userExistsCheck(Long id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("User", id));
    }
  }

  private boolean userWithEmailAlreadyExists(String email) {
    return userRepository.findByEmail(email).isPresent();
  }

  private User retrieveUserFromRepository(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("User", userId)));
  }

  private TeacherDetails retrieveTeacherDetailsFromRepository(Long userId) {
    return teacherDetailsRepository
        .findByUserId(userId)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.teacherDetailsNotFound(userId)));
  }

  private Page<Course> retrieveUserCourses(User user, Pageable pageable) {
    Page<Course> courses;

    switch (user.getRole()) {
      case ROLE_STUDENT:
        courses = courseRepository.findCoursesByUserId(user.getId(), pageable);
        break;
      case ROLE_TEACHER:
        courses = courseRepository.findCoursesByTeacherId(user.getId(), pageable);
        break;
      default:
        throw new EntityNotFoundException(
            "User with ID: " + user.getId() + " is not a student or teacher");
    }

    return courses;
  }
}
