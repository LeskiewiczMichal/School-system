package com.leskiewicz.schoolsystem.degree.utils;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;

public interface DegreeMapper {

  DegreeDto convertToDto(Degree degree);
}
