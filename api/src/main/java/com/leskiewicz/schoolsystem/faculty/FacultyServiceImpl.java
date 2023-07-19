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
public class FacultyServiceImpl implements FacultyService{

    private final FacultyRepository facultyRepository;

    @Override
    public Faculty getByName(String name) {
        return facultyRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.FACULTY_WITH_NAME_NOT_FOUND));
    }

    @Override
    public Degree getDegreeByTitleAndFieldOfStudy(Faculty faculty, DegreeTitle title, String fieldOfStudy) {
        List<Degree> degrees = faculty.getDegrees();
        System.out.println(degrees);

        Optional<Degree> degree = degrees.stream()
                .filter(d -> d.getTitle().equals(title) && d.getFieldOfStudy().equals(fieldOfStudy))
                .findFirst();

        return degree.orElseThrow(() -> new EntityNotFoundException(ErrorMessages.DEGREE_NOT_ON_FACULTY));
    }
}
