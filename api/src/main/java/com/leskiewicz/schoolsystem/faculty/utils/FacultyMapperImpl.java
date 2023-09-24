package com.leskiewicz.schoolsystem.faculty.utils;

import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.utils.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FacultyMapperImpl implements Mapper<Faculty, FacultyDto> {

  private final Logger logger = LoggerFactory.getLogger(FacultyMapperImpl.class);

  @Override
  public FacultyDto mapToDto(Faculty faculty) {
    ValidationUtils.validate(faculty);

    if (faculty.getId() == null) {
      throw new IllegalArgumentException(
          ErrorMessages.objectInvalidPropertyMissing("Faculty", "id"));
    }

    logger.debug("Converted Faculty entity with ID: {} to FacultyDto", faculty.getId());
    return new FacultyDto(faculty.getId(), faculty.getName());
  }

  @Override
  public Page<FacultyDto> mapPageToDto(Page<Faculty> users) {
    return users.map(this::mapToDto);
  }
}
