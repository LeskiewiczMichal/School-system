package com.leskiewicz.schoolsystem.course.utils;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import org.springframework.data.domain.Page;

public interface CourseMapper {

  CourseDto convertToDto(Course course);

  Page<CourseDto> mapPageToDto(Page<Course> courses);
}
