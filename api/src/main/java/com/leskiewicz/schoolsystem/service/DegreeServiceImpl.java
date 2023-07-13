package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.model.Degree;
import com.leskiewicz.schoolsystem.repository.DegreeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;

    @Override
    public Degree getByTitleAndFieldOfStudy(String title, String fieldOfStudy) {
        return degreeRepository.findByTitleAndFieldOfStudy(title, fieldOfStudy).orElse(null);
    }
}
