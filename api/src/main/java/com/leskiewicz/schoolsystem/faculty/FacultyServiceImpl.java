package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
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
        return facultyRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Faculty with given name not found"));
    }

    @Override
    public Degree getDegreeByTitleAndFieldOfStudy(Faculty faculty, DegreeTitle title, String fieldOfStudy) {
        List<Degree> degrees = faculty.getDegrees();

        Optional<Degree> degree = degrees.stream()
                .filter(d -> d.getTitle().equals(title) && d.getFieldOfStudy().equals(fieldOfStudy))
                .findFirst();

        return degree.orElseThrow(() -> new EntityNotFoundException("Degree with given title and field of study not found on chosen faculty"));
    }
}
