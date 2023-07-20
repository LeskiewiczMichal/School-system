package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DegreeService {

  Degree getById(Long id);

  Page<Degree> getDegrees(Pageable pageable);

  Degree getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);
}
