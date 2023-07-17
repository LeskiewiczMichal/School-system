package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.model.Degree;
import com.leskiewicz.schoolsystem.model.enums.DegreeTitle;
import com.leskiewicz.schoolsystem.repository.DegreeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;

    @Override
    public Degree getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy) {
        return degreeRepository.findByTitleAndFieldOfStudy(title, fieldOfStudy).orElseThrow(() -> new EntityNotFoundException("Degree with given title and field of study not found"));
    }
}
