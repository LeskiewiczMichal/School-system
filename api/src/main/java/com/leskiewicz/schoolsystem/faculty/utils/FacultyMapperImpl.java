package com.leskiewicz.schoolsystem.faculty.utils;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.utils.ValidationUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class FacultyMapperImpl implements FacultyMapper {

  private final Logger logger = LoggerFactory.getLogger(FacultyMapperImpl.class);

  @Override
  public FacultyDto convertToDto(Faculty faculty) {
    // Perform manual validation
    ValidationUtils.validate(faculty);
    if (faculty.getId() == null) {
      throw new IllegalArgumentException("Invalid Faculty object: " + "id missing");
    }

    logger.debug("Converted Faculty entity with ID: {} to FacultyDto", faculty.getId());
    return new FacultyDto(faculty.getId(), faculty.getName());
  }
}
