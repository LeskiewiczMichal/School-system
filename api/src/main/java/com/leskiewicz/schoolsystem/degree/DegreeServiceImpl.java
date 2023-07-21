package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import com.leskiewicz.schoolsystem.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DegreeServiceImpl implements DegreeService {

  private final DegreeRepository degreeRepository;
  private final FacultyService facultyService;
  private final Logger logger = LoggerFactory.getLogger(DegreeServiceImpl.class);

  @Override
  public Degree getById(Long id) {
    return degreeRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Degree", id)));
  }

  @Override
  public Page<Degree> getDegrees(Pageable pageable) {
    return degreeRepository.findAll(pageable);
  }

  @Override
  public Degree getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy) {
    return degreeRepository
        .findByTitleAndFieldOfStudy(title, fieldOfStudy)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Degree with given title and field of study not found"));
  }

  @Override
  public Degree createDegree(CreateDegreeRequest request) {
    Faculty faculty = facultyService.getByName(request.getFacultyName());

    Degree degree =
        Degree.builder()
            .title(request.getTitle())
            .fieldOfStudy(StringUtils.capitalizeFirstLetterOfEveryWord(request.getFieldOfStudy()))
            .faculty(faculty)
            .build();

    ValidationUtils.validate(degree);
    degreeRepository.save(degree);
    logger.info("Created degree: {}", degree);
    return degree;
  }
}
