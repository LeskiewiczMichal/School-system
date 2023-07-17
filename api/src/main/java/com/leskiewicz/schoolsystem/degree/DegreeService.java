package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;

public interface DegreeService {

    Degree getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);
}
