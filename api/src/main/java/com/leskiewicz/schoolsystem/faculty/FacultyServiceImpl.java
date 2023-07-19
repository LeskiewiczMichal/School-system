package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService {

  private final FacultyRepository facultyRepository;

  @Override
  public Faculty getByName(String name) {
    return facultyRepository.findByName(name).orElseThrow(
        () -> new EntityNotFoundException(ErrorMessages.objectWithNameNotFound("Faculty", name)));
  }

  @Override
  public Degree getDegreeByTitleAndFieldOfStudy(Faculty faculty, DegreeTitle title,
      String fieldOfStudy) {
    List<Degree> degrees = faculty.getDegrees();

    // Get degree if it is in the faculty
    Optional<Degree> degree = degrees.stream()
        .filter(d -> d.getTitle().equals(title) && d.getFieldOfStudy().equals(fieldOfStudy))
        .findFirst();

    return degree.orElseThrow(() -> new EntityNotFoundException(
        ErrorMessages.degreeNotOnFaculty(fieldOfStudy, title, faculty.getName())));
  }
}
