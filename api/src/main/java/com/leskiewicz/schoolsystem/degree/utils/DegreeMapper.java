package com.leskiewicz.schoolsystem.degree.utils;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DegreeMapper {

  private static final Logger logger = LoggerFactory.getLogger(DegreeMapper.class);

  public static DegreeDto convertToDto(Degree degree) {
    ValidationUtils.validate(degree);
    if (degree.getId() == null) {
      throw new IllegalArgumentException(
          ErrorMessages.objectInvalidPropertyMissing("Degree", "id"));
    }

    logger.debug("Converted Degree entity with ID: {} to DegreeDto", degree.getId());
    return new DegreeDto(degree.getId(), degree.getTitle(), degree.getFieldOfStudy());
  }

}
