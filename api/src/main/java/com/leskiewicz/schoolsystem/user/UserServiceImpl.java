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

  private final FacultyService facultyService;
  private final FileService fileService;
  private final PasswordEncoder passwordEncoder;

  // Repositories
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final TeacherDetailsRepository teacherDetailsRepository;

  // Mappers
  private final UserMapper userMapper;
  private final CourseMapper courseMapper;
  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override
  public UserDto getById(Long id) {
    return userMapper.convertToDto(
        userRepository
            .findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("User", id))));
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
    return users.map(userMapper::convertToDto);
  }

  @Override
  public void addUser(User user) {
    ValidationUtils.validate(user);
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new UserAlreadyExistsException(
          ErrorMessages.userWithEmailAlreadyExists(user.getEmail()));
    }

    logger.info("Adding new user with email: " + user.getEmail());
    userRepository.save(user);
  }

  @Override
  public UserDto updateUser(PatchUserRequest request, Long userId) {
    // Find user
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("User", userId)));

    // Update degree
    if (request.getDegreeField() != null || request.getDegreeTitle() != null) {
      logger.debug("Updating degree of user with ID: {}", userId);

      // If faculty data not complete, throw an error
      if ((request.getDegreeField() != null && request.getDegreeTitle() == null)
          || (request.getDegreeField() == null && request.getDegreeTitle() != null)) {
        throw new MissingFieldException(
            ErrorMessages.objectWasNotUpdated("User")
                + ". Required fields to update degree: title and field of study");
      }

      // If user is changing faculty, validate the new one
      Faculty userFaculty;
      if (request.getFacultyName() != null) {
        userFaculty = facultyService.getByName(request.getFacultyName());
      } else {
        userFaculty = user.getFaculty();
      }

      try {
        Degree degree =
            facultyService.getDegreeByTitleAndFieldOfStudy(
                userFaculty, request.getDegreeTitle(), request.getDegreeField());
        user.setDegree(degree);
      } catch (EntityNotFoundException e) {
        throw new EntityNotFoundException(
            ErrorMessages.objectWasNotUpdated("User") + ". " + e.getMessage());
      }
    }

    // Change faculty
    if (request.getFacultyName() != null) {
      // TODO: add logic for user changing faculty
      logger.debug("Updating faculty of user with ID: {}", userId);
      try {
        Faculty faculty = facultyService.getByName(request.getFacultyName());
        Degree degree =
            facultyService.getDegreeByTitleAndFieldOfStudy(
                faculty, user.getDegree().getTitle(), user.getDegree().getFieldOfStudy());
        user.setFaculty(faculty);
        user.setDegree(degree);
      } catch (EntityNotFoundException e) {
        throw new EntityNotFoundException(
            ErrorMessages.objectWasNotUpdated("User") + ". " + e.getMessage());
      }
    }

    // Change email
    if (request.getEmail() != null) {
      logger.debug("Updating email of user with ID: {}", userId);
      Optional<User> sameEmailUser = userRepository.findByEmail(request.getEmail());
      if (sameEmailUser.isPresent()) {
        throw new UserAlreadyExistsException(
            ErrorMessages.objectWasNotUpdated("User")
                + ". "
                + ErrorMessages.userWithEmailAlreadyExists(request.getEmail()));
      }
      user.setEmail(request.getEmail());
    }

    // Change first name
    if (request.getFirstName() != null) {
      logger.debug("Updating first name of user with ID: {}", userId);
      user.setFirstName(request.getFirstName());
    }

    // Change last name
    if (request.getLastName() != null) {
      logger.debug("Updating last name of user with ID: {}", userId);
      user.setLastName(request.getLastName());
    }

    // Change password
    if (request.getPassword() != null) {
      logger.debug("Updating password of user with ID: {}", userId);
      String encodedPassword = passwordEncoder.encode(request.getPassword());
      user.setPassword(encodedPassword);
    }

    ValidationUtils.validate(user);
    userRepository.save(user);
    return userMapper.convertToDto(user);
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
    // Retrieve user
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("User", userId)));

    Page<Course> courses;
    if (user.getRole() == Role.ROLE_STUDENT) {
      courses = courseRepository.findCoursesByUserId(userId, pageable);
    } else if (user.getRole() == Role.ROLE_TEACHER) {
      courses = courseRepository.findCoursesByTeacherId(userId, pageable);
    } else {
      throw new EntityNotFoundException("User with ID: " + userId + " is not a student or teacher");
    }

    return courses.map(courseMapper::convertToDto);
  }

  @Override
  public TeacherDetails getTeacherDetails(Long userId) {
    userExistsCheck(userId);

    return teacherDetailsRepository
        .findByUserId(userId)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.teacherDetailsNotFound(userId)));
  }

  @Override
  public TeacherDetails updateTeacherDetails(PatchTeacherDetailsRequest request, Long userId) {
    // Retrieve teacher details for given user
    TeacherDetails teacherDetails =
        teacherDetailsRepository
            .findByUserId(userId)
            .orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.teacherDetailsNotFound(userId)));

    // Update teacher details
    if (request.getTitle() != null) {
      logger.debug("Updating academic title of teacher with ID: {}", userId);
      teacherDetails.setTitle(request.getTitle());
    }
    if (request.getBio() != null) {
      logger.debug("Updating bio of teacher with ID: {}", userId);
      teacherDetails.setBio(request.getBio());
    }
    if (request.getDegreeField() != null) {
      logger.debug("Updating degree field of teacher with ID: {}", userId);
      teacherDetails.setDegreeField(request.getDegreeField());
    }
    if (request.getTutorship() != null) {
      logger.debug("Updating tutorship of teacher with ID: {}", userId);
      teacherDetails.setTutorship(request.getTutorship());
    }

    ValidationUtils.validate(teacherDetails);
    teacherDetailsRepository.save(teacherDetails);
    return teacherDetails;
  }

  @Override
  public Page<UserDto> search(String lastName, String firstName, Role role, Pageable pageable) {
    Page<User> users =
        userRepository.searchUsersByLastNameAndFirstNameAndRole(
            lastName, firstName, role, pageable);

    return users.map(userMapper::convertToDto);
  }

  @Override
  public void addImage(Long userId, MultipartFile image) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("User", userId)));

    // Handle the image if available
    if (image != null) {
      String filename = fileService.uploadImage(image);
      user.setProfilePictureName(filename);
    } else {
      throw new IllegalArgumentException("Image cannot be null");
    }

    userRepository.save(user);
    logger.info("Updated profile picture of user with ID: {}", userId);
  }

  private void userExistsCheck(Long id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("User", id));
    }
  }
}
