package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.MissingFieldException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.error.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final DegreeService degreeService;
  private final FacultyService facultyService;
  private final PasswordEncoder passwordEncoder;
  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override
  public User getById(Long id) {
    logger.debug("Retrieving user with id: {}", id);
    return userRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("User", id)));
  }

  @Override
  public User getByEmail(String email) {
    logger.debug("Retrieving user with email: {}", StringUtils.maskEmail(email));
    return userRepository.findByEmail(email).orElseThrow(() ->
        new EntityNotFoundException(ErrorMessages.objectWithEmailNotFound("User", email)));
  }

  @Override
  public Page<User> getUsers(Pageable pageable) {
    logger.debug("Retrieving users with pagination parameters: {}", pageable);
    return userRepository.findAll(pageable);
  }

  @Override
  public List<UserDto> toUserDtos(Page<User> usersPage) {
    return usersPage.getContent().stream()
        .map(userMapper::convertToDto)
        .collect(Collectors.toList());
  }

  @Override
  public void addUser(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new UserAlreadyExistsException(
          ErrorMessages.userWithEmailAlreadyExists(user.getEmail()));
    }
    logger.info("Adding new user with email: " + user.getEmail());
    userRepository.save(user);
  }

  @Override
  public User updateUser(PatchUserRequest request, Long userId) {
    // Find user
    User user = userRepository.findById(userId).orElseThrow(() ->
        new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("User", userId)));

    logger.info("Updating user with ID: {}", userId);

    // Update degree
    if (request.getDegreeField() != null || request.getDegreeTitle() != null) {
      logger.info("Updating degree of user with ID: {}", userId);

      // If faculty data not complete, throw an error
      if ((request.getDegreeField() != null && request.getDegreeTitle() == null) || (
          request.getDegreeField() == null && request.getDegreeTitle() != null)) {
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
        Degree degree = facultyService.getDegreeByTitleAndFieldOfStudy(userFaculty,
            request.getDegreeTitle(), request.getDegreeField());
        user.setDegree(degree);
      } catch (EntityNotFoundException e) {
        logger.warn(ErrorMessages.objectWasNotUpdated("User") + ". " + e.getMessage());
        throw e;
      }
    }

    // Change faculty
    if (request.getFacultyName() != null) {
      // TODO: add logic for user changing faculty
      logger.info("Updating faculty of user with ID: {}", userId);
      try {
        Faculty faculty = facultyService.getByName(request.getFacultyName());
        Degree degree = facultyService.getDegreeByTitleAndFieldOfStudy(faculty,
            user.getDegree().getTitle(), user.getDegree().getFieldOfStudy());
        user.setFaculty(faculty);
        user.setDegree(degree);
      } catch (EntityNotFoundException e) {
        logger.warn(ErrorMessages.objectWasNotUpdated("User") + ". " + e.getMessage());
        throw e;
      }
    }

    // Change email
    if (request.getEmail() != null) {
      logger.info("Updating email of user with ID: {}", userId);
      Optional<User> sameEmailUser = userRepository.findByEmail(request.getEmail());
      if (sameEmailUser.isPresent()) {
        throw new UserAlreadyExistsException(
            ErrorMessages.objectWasNotUpdated("User") + ". "
                + ErrorMessages.userWithEmailAlreadyExists(request.getEmail()));
      }
      user.setEmail(request.getEmail());
    }

    // Change first name
    if (request.getFirstName() != null) {
      logger.info("Updating first name of user with ID: {}", userId);
      user.setFirstName(request.getFirstName());
    }

    // Change last name
    if (request.getLastName() != null) {
      logger.info("Updating last name of user with ID: {}", userId);
      user.setLastName(request.getLastName());
    }

    // Change password
    if (request.getPassword() != null) {
      logger.info("Updating password of user with ID: {}", userId);
      String encodedPassword = passwordEncoder.encode(request.getPassword());
      user.setPassword(encodedPassword);
    }

    userRepository.save(user);
    return user;
  }

}
