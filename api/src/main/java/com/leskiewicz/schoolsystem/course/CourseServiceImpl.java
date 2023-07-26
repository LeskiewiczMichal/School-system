package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.DuplicateEntityException;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

  // Repositories
  private CourseRepository courseRepository;
  private FacultyRepository facultyRepository;
  private UserRepository userRepository;

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

  @Override
  public CourseDto createCourse(CreateCourseRequest createCourseRequest) {
    // Retrieve needed entities
    userRepository
        .findById(createCourseRequest.getTeacherId())
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    ErrorMessages.objectWithIdNotFound(
                        "User", createCourseRequest.getTeacherId())));
    Faculty faculty =
        facultyRepository
            .findById(createCourseRequest.getFacultyId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound(
                            "Faculty", createCourseRequest.getFacultyId())));

    // Create new course
    Course newCourse =
        Course.builder()
            .title(createCourseRequest.getTitle())
            .duration_in_hours(createCourseRequest.getDurationInHours())
            .faculty(faculty)
            .teacher(userRepository.findById(createCourseRequest.getTeacherId()).get())
            .build();

    // Check if the course doesn't already exist
    List<Course> coursesWithTheSameTitle =
        courseRepository.findByTitleContainingIgnoreCase(createCourseRequest.getTitle());
    if (coursesWithTheSameTitle.contains(newCourse)) {
      throw new DuplicateEntityException(ErrorMessages.objectAlreadyExists("Course"));
    }

    ValidationUtils.validate(newCourse);
    courseRepository.save(newCourse);

    return courseMapper.convertToDto(newCourse);
  }
}
