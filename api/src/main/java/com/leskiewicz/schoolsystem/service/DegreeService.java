package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.model.Degree;
import com.leskiewicz.schoolsystem.model.enums.DegreeTitle;

public interface DegreeService {

    Degree getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);
}
