package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService{

    private final FacultyRepository facultyRepository;

    @Override
    public Faculty getByName(String name) {
        return facultyRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Faculty with given name not found"));
    }

    @Override
    public void validateDegreeForFaculty(Faculty faculty, Degree degree) {
        if (!faculty.getDegrees().contains(degree)) {
            throw new IllegalArgumentException("Degree " + degree.getTitle() + " in the field of " + degree.getFieldOfStudy() + " is not offered by the faculty " + faculty.getName());
        }
    }
}
