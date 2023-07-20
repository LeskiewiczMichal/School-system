package com.leskiewicz.schoolsystem.faculty.utils;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacultyMapper {

  private static final Logger logger = LoggerFactory.getLogger(FacultyMapper.class);

  public static FacultyDto convertToDto(Faculty faculty) {
    // Perform manual validation
    ValidationUtils.validate(faculty);
    if (faculty.getId() == null) {
      throw new IllegalArgumentException("Invalid Faculty object: " + "id missing");
    }

    logger.debug("Converted Faculty entity with ID: {} to FacultyDto", faculty.getId());
    return new FacultyDto(faculty.getId(), faculty.getName());
  }
}
