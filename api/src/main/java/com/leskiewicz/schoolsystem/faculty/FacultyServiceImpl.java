package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.Mapper;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import com.leskiewicz.schoolsystem.utils.Support;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService {

  private final Support support;
  // Repositories
  private final FacultyRepository facultyRepository;
  private final CourseRepository courseRepository;
  private final DegreeRepository degreeRepository;
  private final UserRepository userRepository;

  // Mappers
  private final DegreeMapper degreeMapper;
  private final FacultyMapper facultyMapper;
  private final Mapper<User, UserDto> userMapper;
  private final CourseMapper courseMapper;

  private final Logger logger = LoggerFactory.getLogger(FacultyController.class);

  @Override
  public FacultyDto getById(Long id) {
    Faculty faculty = retrieveFacultyFromRepositoryById(id);
    return facultyMapper.mapToDto(faculty);
  }

  @Override
  public Faculty getByName(String name) {
    return facultyRepository
        .findByName(name)
        .orElseThrow(
            () ->
                new EntityNotFoundException(ErrorMessages.objectWithNameNotFound("Faculty", name)));
  }

  @Override
  public Page<FacultyDto> getFaculties(Pageable pageable) {
    Page<Faculty> faculties = facultyRepository.findAll(pageable);
    return facultyMapper.mapPageToDto(faculties);
  }

  @Override
  public Degree getDegreeByTitleAndFieldOfStudy(
      Faculty faculty, DegreeTitle title, String fieldOfStudy) {
    List<Degree> degrees = faculty.getDegrees();
    Optional<Degree> degree = findDegreeByTitleAndFieldOfStudy(degrees, title, fieldOfStudy);

    return degree.orElseThrow(
        () ->
            new EntityNotFoundException(
                ErrorMessages.degreeNotOnFaculty(fieldOfStudy, title, faculty.getName())));
  }

  private Optional<Degree> findDegreeByTitleAndFieldOfStudy(List<Degree> degrees, DegreeTitle title, String fieldOfStudy) {
    return degrees.stream()
            .filter(d -> d.getTitle().equals(title) && d.getFieldOfStudy().equals(fieldOfStudy))
            .findFirst();
  }

  @Override
  public FacultyDto createFaculty(CreateFacultyRequest request) {
    facultyWithNameAlreadyExistsCheck(request.name());
    Faculty faculty = buildFaculty(request);
    faculty = facultyRepository.save(faculty);
    support.notifyCreated("Faculty", faculty.getId());

    return facultyMapper.mapToDto(faculty);
  }

  private Faculty buildFaculty(CreateFacultyRequest request) {
   Faculty newFaculty = Faculty.builder()
            .name(StringUtils.capitalizeFirstLetterOfEveryWord(request.name()))
            .build();
   ValidationUtils.validate(newFaculty);
    return newFaculty;
  }

  @Override
  public FacultyDto updateFaculty(PatchFacultyRequest request, Long facultyId) {
    Faculty faculty = retrieveFacultyFromRepositoryById(facultyId);

    if (request.name() != null) {
      if (facultyRepository.findByName(request.name()).isPresent()) {
        throw new EntityAlreadyExistsException(
            ErrorMessages.objectWithPropertyAlreadyExists("Faculty", "name", request.name()));
      }

      logger.debug("Updating faculty name from {} to {}", faculty.getName(), request.name());
      faculty.setName(StringUtils.capitalizeFirstLetterOfEveryWord(request.name()));
    }

    facultyRepository.save(faculty);
    logger.info("Updated faculty with id: {}", facultyId);
    return facultyMapper.mapToDto(faculty);
  }

  @Override
  public Page<UserDto> getFacultyUsers(Long facultyId, Pageable pageable, Role role) {
    facultyExistsCheck(facultyId);
    Page<User> users = userRepository.findUsersByFacultyId(facultyId, pageable, role);
    return userMapper.mapPageToDto(users);
  }

  @Override
  public Page<DegreeDto> getFacultyDegrees(Long facultyId, Pageable pageable) {
    facultyExistsCheck(facultyId);
    Page<Degree> degrees = degreeRepository.findDegreesByFacultyId(facultyId, pageable);
    return degreeMapper.mapPageToDto(degrees);
  }

  @Override
  public Page<CourseDto> getFacultyCourses(Long facultyId, Pageable pageable) {
    facultyExistsCheck(facultyId);
    Page<Course> courses = courseRepository.findCoursesByFacultyId(facultyId, pageable);
    return courseMapper.mapPageToDto(courses);
  }

  private void facultyExistsCheck(Long facultyId) {
    if (!facultyRepository.existsById(facultyId)) {
      throw new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Faculty", facultyId));
    }
  }

  private void facultyWithNameAlreadyExistsCheck(String facultyName) {
    if (facultyRepository.findByName(facultyName).isPresent()) {
      throw new EntityAlreadyExistsException(
          ErrorMessages.objectWithPropertyAlreadyExists("Faculty", "name", facultyName));
    }
  }

  private Faculty retrieveFacultyFromRepositoryById(Long facultyId) {
    return facultyRepository
        .findById(facultyId)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    ErrorMessages.objectWithIdNotFound("Faculty", facultyId)));
  }
}
