package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.error.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

  private CourseRepository courseRepository;

  @Override
  public Course getById(Long id) {
    return courseRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Course", id)));
  }

  @Override
  public void addStudentToCourse(Long courseId, Principal principal) {
    System.out.println(principal);
  }
}
