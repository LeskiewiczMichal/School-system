package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface CourseService {

  CourseDto getById(Long id);

  Page<CourseDto> getCourses(Pageable pageable);

  CourseDto createCourse(CreateCourseRequest createCourseRequest);

  Page<UserDto> getCourseStudents(Long courseId, Pageable pageable);

  void addStudentToCourse(Long userId, Long courseId);

  void deleteCourse(Long courseId);
}
