package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
  private final UserMapper userMapper;
  private final DegreeMapper degreeMapper;
  private final Logger logger = LoggerFactory.getLogger(DegreeServiceImpl.class);

  @Override
  public DegreeDto getById(Long id) {
    return degreeMapper.convertToDto(degreeRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Degree", id))));
  }

  @Override
  public Page<DegreeDto> getDegrees(Pageable pageable) {
    Page<Degree> degrees = degreeRepository.findAll(pageable);
    return degrees.map(degreeMapper::convertToDto);
  }

  @Override
  public List<Degree> getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy) {
    return degreeRepository.findByTitleAndFieldOfStudy(title, fieldOfStudy);
  }

  @Override
  public DegreeDto createDegree(CreateDegreeRequest request) {
    // If the same degree already exist, throws error
    if (degreeRepository
        .findByFacultyNameAndTitleAndFieldOfStudy(
            request.getFacultyName(), request.getTitle(), request.getFieldOfStudy())
        .isPresent()) {
      throw new EntityAlreadyExistsException(
          ErrorMessages.objectWithPropertyAlreadyExists(
              "Degree",
              "title",
              request.getTitle()
                  + " in "
                  + request.getFieldOfStudy()
                  + " on faculty: "
                  + request.getFacultyName()));
    }

    // Retrieves faculty from database
    Faculty faculty = facultyService.getByName(request.getFacultyName());

    // Creates degree
    Degree degree =
        Degree.builder()
            .title(request.getTitle())
            .fieldOfStudy(StringUtils.capitalizeFirstLetterOfEveryWord(request.getFieldOfStudy()))
            .faculty(faculty)
            .build();

    ValidationUtils.validate(degree);
    degreeRepository.save(degree);
    logger.info("Created degree: {}", degree);

    return degreeMapper.convertToDto(degree);
  }


}
