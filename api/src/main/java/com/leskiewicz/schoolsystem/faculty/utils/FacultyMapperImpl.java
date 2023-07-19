package com.leskiewicz.schoolsystem.faculty.utils;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class FacultyMapperImpl implements FacultyMapper {

    private final Validator validator;
    private final Logger logger = LoggerFactory.getLogger(FacultyMapperImpl.class);

    @Override
    public FacultyDto convertToDto(Faculty faculty) {
        // Perform manual validation
        Set<ConstraintViolation<Faculty>> violations = validator.validate(faculty);
        if (!violations.isEmpty()) {
            logger.error("An error occurred while converting a Faculty object to a FacultyDto object: Faculty = {}, Violations = {}", faculty, violations);
            throw new IllegalArgumentException("Invalid Faculty object: " + violations.toString());
        }
        if (faculty.getId() == null) {
            logger.error("An error occurred while converting a Faculty object to a FacultyDto object: Faculty = {}, no id provided", faculty);
            throw new IllegalArgumentException("Invalid Faculty object: " + "id missing");
        }

        return new FacultyDto(faculty.getId(), faculty.getName());
    }
}
