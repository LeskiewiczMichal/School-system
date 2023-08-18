package com.leskiewicz.schoolsystem.course.utils;

import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.user.User;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CourseMapperImpl implements CourseMapper {

  private final Logger logger = org.slf4j.LoggerFactory.getLogger(CourseMapperImpl.class);

  @Override
  public CourseDto convertToDto(Course course) {
    // Perform manual validation
    ValidationUtils.validate(course);
    if (course.getId() == null) {
      throw new IllegalArgumentException(
          ErrorMessages.objectInvalidPropertyMissing("Course", "id"));
    }

    User courseTeacher = course.getTeacher();
    Faculty courseFaculty = course.getFaculty();

    CourseDto courseDto =
        CourseDto.builder()
            .id(course.getId())
            .title(course.getTitle())
            .durationInHours(course.getDuration_in_hours())
            .faculty(courseFaculty.getName())
            .teacher(courseTeacher.getFirstName() + " " + courseTeacher.getLastName())
            .facultyId(courseFaculty.getId())
            .teacherId(courseTeacher.getId())
            .language(course.getLanguage())
            .scope(course.getScope())
            .build();

    logger.debug("Converted Course entity with ID: {} to CourseDto", course.getId());
    return courseDto;
  }
}
