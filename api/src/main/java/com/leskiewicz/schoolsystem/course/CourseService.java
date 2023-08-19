package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

public interface CourseService {

  CourseDto getById(Long id);

  Page<CourseDto> getCourses(Pageable pageable);

  CourseDto createCourse(CreateCourseRequest createCourseRequest);

  Page<UserDto> getCourseStudents(Long courseId, Pageable pageable);

  void addStudentToCourse(Long userId, Long courseId);

  void deleteCourse(Long courseId);

  void storeFile(MultipartFile file, Long courseId) throws IOException;

  Page<File> getCourseFiles(Long courseId, Pageable pageable);

  String getCourseDescription(Long courseId);

  boolean isUserEnrolled(Long courseId, Long userId);
}
