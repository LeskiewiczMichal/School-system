package com.leskiewicz.schoolsystem.faculty.utils;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;

public interface FacultyMapper {

  FacultyDto convertToDto(Faculty faculty);
}
