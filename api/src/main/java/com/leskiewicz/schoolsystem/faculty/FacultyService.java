package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;

public interface FacultyService {

    Faculty getByName(String name);

    Degree getDegreeByTitleAndFieldOfStudy(Faculty faculty, DegreeTitle title, String fieldOfStudy);
}
