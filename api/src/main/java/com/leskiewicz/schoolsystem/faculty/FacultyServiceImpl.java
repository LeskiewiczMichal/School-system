package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.degree.utils.DegreeModelAssembler;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserModelAssembler;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import com.leskiewicz.schoolsystem.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.apache.catalina.UserDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService {

  private final FacultyRepository facultyRepository;
  private final FacultyModelAssembler facultyModelAssembler;
  private final DegreeModelAssembler degreeModelAssembler;
  private final UserModelAssembler userModelAssembler;
  private final DegreeMapper degreeMapper;
  private final FacultyMapper facultyMapper;
  private final Logger logger = LoggerFactory.getLogger(FacultyController.class);

  @Override
  public Faculty getById(Long id) {
    return facultyRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Faculty", id)));
  }

  @Override
  public Faculty getByName(String name) {
    return facultyRepository
        .findByName(name)
        .orElseThrow(
            () ->
                new EntityNotFoundException(ErrorMessages.objectWithNameNotFound("Faculty", name)));
  }

  @Override
  public Page<FacultyDto> getFaculties(Pageable pageable) {
    Page<Faculty> faculties = facultyRepository.findAll(pageable);
    return faculties.map(facultyModelAssembler::toModel);
  }

  @Override
  public Degree getDegreeByTitleAndFieldOfStudy(
      Faculty faculty, DegreeTitle title, String fieldOfStudy) {
    List<Degree> degrees = faculty.getDegrees();

    // Get degree if it is in the faculty
    Optional<Degree> degree =
        degrees.stream()
            .filter(d -> d.getTitle().equals(title) && d.getFieldOfStudy().equals(fieldOfStudy))
            .findFirst();

    return degree.orElseThrow(
        () ->
            new EntityNotFoundException(
                ErrorMessages.degreeNotOnFaculty(fieldOfStudy, title, faculty.getName())));
  }

  @Override
  public FacultyDto createFaculty(CreateFacultyRequest request) {
    if (facultyRepository.findByName(request.getName()).isPresent()) {
      throw new EntityAlreadyExistsException(
          ErrorMessages.objectWithPropertyAlreadyExists("Faculty", "name", request.getName()));
    }

    Faculty faculty =
        Faculty.builder()
            .name(StringUtils.capitalizeFirstLetterOfEveryWord(request.getName()))
            .build();
    ValidationUtils.validate(faculty);
    facultyRepository.save(faculty);
    logger.info("Created new faculty with name: {}", faculty.getName());

    return facultyMapper.convertToDto(faculty);
  }

  @Override
  public FacultyDto updateFaculty(PatchFacultyRequest request, Long facultyId) {
    Faculty faculty =
        facultyRepository
            .findById(facultyId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("Faculty", facultyId)));

    if (request.getName() != null) {
      if (facultyRepository.findByName(request.getName()).isPresent()) {
        throw new EntityAlreadyExistsException(
            ErrorMessages.objectWithPropertyAlreadyExists("Faculty", "name", request.getName()));
      }

      logger.debug("Updating faculty name from {} to {}", faculty.getName(), request.getName());
      faculty.setName(StringUtils.capitalizeFirstLetterOfEveryWord(request.getName()));
    }

    facultyRepository.save(faculty);
    logger.info("Updated faculty with id: {}", facultyId);
    return facultyModelAssembler.toModel(faculty);
  }

  @Override
  public Page<UserDto> getFacultyUsers(Long facultyId, Pageable pageable, Role role) {
    facultyExistsCheck(facultyId);
    Page<User> users = facultyRepository.findFacultyUsers(facultyId, pageable, role);
    return users.map(userModelAssembler::toModel);
  }

  @Override
  public Page<DegreeDto> getFacultyDegrees(Long facultyId, Pageable pageable) {
    facultyExistsCheck(facultyId);
    Page<Degree> degrees = facultyRepository.findFacultyDegrees(facultyId, pageable);
    return degrees.map(degreeMapper::convertToDto);
  }

  private void facultyExistsCheck(Long facultyId) {
    if (!facultyRepository.existsById(facultyId)) {
      throw new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Faculty", facultyId));
    }
  }
}
