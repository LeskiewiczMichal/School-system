package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;

public interface FacultyService {

    Faculty getByName(String name);
    public void validateDegreeForFaculty(Faculty faculty, Degree degree);
}
