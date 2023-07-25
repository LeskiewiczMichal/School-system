package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface CourseService {

  CourseDto getById(Long id);

  Page<CourseDto> getCourses(Pageable pageable);
}
