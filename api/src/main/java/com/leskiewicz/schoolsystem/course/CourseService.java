package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;

import java.security.Principal;

public interface CourseService {

    CourseDto getById(Long id);
}
