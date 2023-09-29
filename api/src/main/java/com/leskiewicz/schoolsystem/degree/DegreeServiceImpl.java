package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.Mapper;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class DegreeServiceImpl implements DegreeService {

  // Repositories
  private final DegreeRepository degreeRepository;
  private final CourseRepository courseRepository;
  private final FacultyService facultyService;
  private final FileService fileService;

  // Mappers
  private final Mapper<User, UserDto> userMapper;
  private final DegreeMapper degreeMapper;
  private final CourseMapper courseMapper;

  private final Logger logger = LoggerFactory.getLogger(DegreeServiceImpl.class);

  @Override
  public DegreeDto getById(Long id) {
    return degreeMapper.convertToDto(
        degreeRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Degree", id))));
  }

  @Override
  public Page<DegreeDto> getDegrees(Pageable pageable) {
    Page<Degree> degrees = degreeRepository.findAll(pageable);
    return degreeMapper.mapPageToDto(degrees);
  }

  @Override
  public List<Degree> getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy) {
    return degreeRepository.findByTitleAndFieldOfStudy(title, fieldOfStudy);
  }

  @Override
  public DegreeDto createDegree(CreateDegreeRequest request) {
    // If the same degree already exist, throws error
    if (degreeRepository
        .findByFacultyNameAndTitleAndFieldOfStudy(
            request.facultyName(), request.title(), request.fieldOfStudy())
        .isPresent()) {
      throw new EntityAlreadyExistsException(
          ErrorMessages.objectWithPropertyAlreadyExists(
              "Degree",
              "title",
              request.title()
                  + " in "
                  + request.fieldOfStudy()
                  + " on faculty: "
                  + request.facultyName()));
    }

    // Retrieves faculty from database
    Faculty faculty = facultyService.getByName(request.facultyName());

    // Creates degree
    Degree degree =
        Degree.builder()
            .title(request.title())
            .fieldOfStudy(StringUtils.capitalizeFirstLetterOfEveryWord(request.fieldOfStudy()))
            .faculty(faculty)
            .description(request.description())
            .language(request.languages())
            .tuitionFeePerYear(request.tuitionFeePerYear())
            .lengthOfStudy(request.lengthOfStudy())
            .build();

    ValidationUtils.validate(degree);
    degreeRepository.save(degree);
    logger.info("Created degree: {}", degree);

    return degreeMapper.convertToDto(degree);
  }

  @Override
  public Page<CourseDto> getDegreeCourses(Long degreeId, Pageable pageable) {
    degreeExistsCheck(degreeId);
    Page<Course> courses = courseRepository.findCoursesByDegreeId(degreeId, pageable);
    return courseMapper.mapPageToDto(courses);
  }

  @Override
  public Page<DegreeDto> search(
      String fieldOfStudy, Long facultyId, DegreeTitle title, Pageable pageable) {
    Page<Degree> degrees =
        degreeRepository.searchByFacultyNameAndTitleAndFieldOfStudy(
            fieldOfStudy, facultyId, title, pageable);

    return degreeMapper.mapPageToDto(degrees);
  }

  @Override
  public void addImage(Long degreeId, MultipartFile image) {
    Degree degree =
        degreeRepository
            .findById(degreeId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("Degree", degreeId)));

    // Handle the image if available
    if (image != null) {
      String filename = fileService.uploadImage(image);
      degree.setImageName(filename);
    } else {
      throw new IllegalArgumentException("Image cannot be null");
    }

    degreeRepository.save(degree);
  }

  private void degreeExistsCheck(Long degreeId) {
    if (!degreeRepository.existsById(degreeId)) {
      throw new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Degree", degreeId));
    }
  }
}
