package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DegreeService {

  Degree getById(Long id);

  Page<Degree> getDegrees(Pageable pageable);

  Degree getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);

  Degree createDegree(CreateDegreeRequest request);
}
