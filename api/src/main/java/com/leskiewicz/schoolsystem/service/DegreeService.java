package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.model.Degree;

public interface DegreeService {

    Degree getByTitleAndFieldOfStudy(String title, String fieldOfStudy);
}
