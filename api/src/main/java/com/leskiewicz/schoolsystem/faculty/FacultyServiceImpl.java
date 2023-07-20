package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import com.leskiewicz.schoolsystem.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService {

  private final FacultyRepository facultyRepository;
  private final Logger logger = LoggerFactory.getLogger(FacultyController.class);

  @Override
  public Faculty getById(Long id) {
    return facultyRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Faculty", id)));
  }

  @Override
  public Faculty getByName(String name) {
    return facultyRepository.findByName(name).orElseThrow(
        () -> new EntityNotFoundException(ErrorMessages.objectWithNameNotFound("Faculty", name)));
  }

  @Override
  public Page<Faculty> getFaculties(Pageable pageable) {
    return facultyRepository.findAll(pageable);
  }

  @Override
  public Degree getDegreeByTitleAndFieldOfStudy(Faculty faculty, DegreeTitle title,
      String fieldOfStudy) {
    List<Degree> degrees = faculty.getDegrees();

    // Get degree if it is in the faculty
    Optional<Degree> degree = degrees.stream()
        .filter(d -> d.getTitle().equals(title) && d.getFieldOfStudy().equals(fieldOfStudy))
        .findFirst();

    return degree.orElseThrow(() -> new EntityNotFoundException(
        ErrorMessages.degreeNotOnFaculty(fieldOfStudy, title, faculty.getName())));
  }

  @Override
  public Faculty createFaculty(CreateFacultyRequest request) {
    if (facultyRepository.findByName(request.getName()).isPresent()) {
      throw new EntityAlreadyExistsException(
          ErrorMessages.objectWithPropertyAlreadyExists("Faculty", "name", request.getName()));
    }

    Faculty faculty = Faculty.builder()
        .name(StringUtils.capitalizeFirstLetterOfEveryWord(request.getName())).build();
    ValidationUtils.validate(faculty);
    facultyRepository.save(faculty);
    logger.info("Created new faculty with name: {}", faculty.getName());

    return faculty;
  }


}
