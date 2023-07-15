package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.model.Course;
import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService{

    private CourseRepository courseRepository;

    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course with given id not found"));
    }

    @Override
    public void addStudentToCourse(Long courseId, Principal principal) {
        System.out.println(principal);
    }
}
