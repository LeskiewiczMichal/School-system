package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.model.Course;
import com.leskiewicz.schoolsystem.model.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

public interface CourseService {

    Course getById(Long id);
    void addStudentToCourse(Long courseId, Principal principal);
}
