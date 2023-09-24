package com.leskiewicz.schoolsystem.degree.utils;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import org.springframework.data.domain.Page;

public interface DegreeMapper {

  DegreeDto convertToDto(Degree degree);

  DegreeDto convertToDtoFull(Degree degree);

  Page<DegreeDto> mapPageToDto(Page<Degree> degreePage);
}
