package com.leskiewicz.schoolsystem.degree.utils;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DegreeMapperImpl implements DegreeMapper {

  private final Logger logger = LoggerFactory.getLogger(DegreeMapperImpl.class);

  @Override
  public DegreeDto convertToDto(Degree degree) {
    ValidationUtils.validate(degree);
    if (degree.getId() == null) {
      throw new IllegalArgumentException(
          ErrorMessages.objectInvalidPropertyMissing("Degree", "id"));
    }

    logger.debug("Converted Degree entity with ID: {} to DegreeDto", degree.getId());

    Faculty degreeFaculty = degree.getFaculty();
    return new DegreeDto(
        degree.getId(),
        degree.getTitle(),
        degree.getFieldOfStudy(),
        degreeFaculty.getName(),
        degree.getLengthOfStudy(),
        degree.getDescription(),
        degree.getTuitionFeePerYear(),
        degree.getLanguages(),
        degreeFaculty.getId());
  }

  @Override
  public DegreeDto convertToDtoFull(Degree degree) {
    DegreeDto degreeDto = convertToDto(degree);
    degreeDto.setDescription(degree.getDescription());
    degreeDto.setTuitionFeePerYear(degree.getTuitionFeePerYear());
    degreeDto.setLanguages(degree.getLanguages());
    return degreeDto;
  }
}
