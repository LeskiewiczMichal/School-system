package com.leskiewicz.schoolsystem.course.utils;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;

public interface CourseMapper {

  CourseDto convertToDto(Course course);
}
