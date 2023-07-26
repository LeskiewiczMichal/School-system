package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

  // Repositories
  private CourseRepository courseRepository;

  // Mappers
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

  @Override
  public Page<CourseDto> getCourses(Pageable pageable) {
    Page<Course> courses = courseRepository.findAll(pageable);
    return courses.map(courseMapper::convertToDto);
  }
}
