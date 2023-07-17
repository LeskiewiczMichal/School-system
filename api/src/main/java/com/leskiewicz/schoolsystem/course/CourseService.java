package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.Course;

import java.security.Principal;

public interface CourseService {

    Course getById(Long id);
    void addStudentToCourse(Long courseId, Principal principal);
}
