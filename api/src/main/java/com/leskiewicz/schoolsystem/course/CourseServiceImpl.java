package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.DuplicateEntityException;
import com.leskiewicz.schoolsystem.error.customexception.EntitiesAlreadyAssociatedException;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

  // Repositories
  private final CourseRepository courseRepository;
  private final FacultyRepository facultyRepository;
  private final UserRepository userRepository;

  // Mappers
  private final CourseMapper courseMapper;
  private final UserMapper userMapper;

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
    User teacher =
        userRepository
            .findById(createCourseRequest.getTeacherId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound(
                            "User", createCourseRequest.getTeacherId())));

    // Check if user is a teacher
    if (!teacher.getRole().equals(Role.ROLE_TEACHER)) {
      throw new IllegalArgumentException(ErrorMessages.userIsNotTeacher(teacher.getId()));
    }

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
            .teacher(teacher)
            .build();

    // Check if the course doesn't already exist
    boolean courseAlreadyExists =
        courseRepository.existsCourseWithAttributes(
            createCourseRequest.getTitle(),
            createCourseRequest.getDurationInHours(),
            createCourseRequest.getTeacherId(),
            createCourseRequest.getFacultyId());

    if (courseAlreadyExists) {
      throw new EntityAlreadyExistsException(ErrorMessages.objectAlreadyExists("Course"));
    }

    // Validate and save new course
    ValidationUtils.validate(newCourse);
    courseRepository.save(newCourse);
    return courseMapper.convertToDto(newCourse);
  }

  @Override
  public Page<UserDto> getCourseStudents(Long courseId, Pageable pageable) {
    courseExistsCheck(courseId);
    Page<User> students = userRepository.findUsersByCourseId(courseId, pageable);
    return students.map(userMapper::convertToDto);
  }

  @Override
  public void addStudentToCourse(Long userId, Long courseId) {
    // Get needed entities
    User student =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("User", userId)));
    if (!student.getRole().equals(Role.ROLE_STUDENT)) {
      throw new IllegalArgumentException(ErrorMessages.userIsNotStudent(student.getId()));
    }

    Course course =
        courseRepository
            .findById(courseId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("Course", courseId)));

    // Check if objects aren't already associated
    if (course.getStudents().contains(student)) {
      throw new EntitiesAlreadyAssociatedException(
          ErrorMessages.objectsAlreadyAssociated(
              "Course", course.getId(), "Student", student.getId()));
    }

    // Add student to course and save
    course.getStudents().add(student);
    courseRepository.save(course);
  }

  @Transactional
  @Override
  public void deleteCourse(Long courseId) {
    courseExistsCheck(courseId);

    courseRepository.deleteDegreeCourseByCourseId(courseId);
    courseRepository.deleteCourseStudentByCourseId(courseId);

    courseRepository.deleteById(courseId);
  }

  private void courseExistsCheck(Long courseId) {
    if (!courseRepository.existsById(courseId)) {
      throw new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Course", courseId));
    }
  }
}
