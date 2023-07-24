package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

  private CourseRepository courseRepository;
  private CourseMapper courseMapper;

  @Override
  public CourseDto getById(Long id) {
    return courseMapper.convertToDto(
        courseRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Course", id))));
  }
}
