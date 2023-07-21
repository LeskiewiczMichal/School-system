package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DegreeService {

  Degree getById(Long id);

  Page<Degree> getDegrees(Pageable pageable);

  List<Degree> getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);

  Degree createDegree(CreateDegreeRequest request);
}
