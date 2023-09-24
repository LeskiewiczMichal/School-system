package com.leskiewicz.schoolsystem.faculty.utils;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import org.springframework.data.domain.Page;

public interface FacultyMapper {

  FacultyDto convertToDto(Faculty faculty);
  Page<FacultyDto> mapPageToDto(Page<Faculty> faculties);
}
