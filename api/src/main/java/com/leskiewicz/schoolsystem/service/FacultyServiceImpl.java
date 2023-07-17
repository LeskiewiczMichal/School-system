package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.model.Faculty;
import com.leskiewicz.schoolsystem.repository.FacultyRepository;
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
}
