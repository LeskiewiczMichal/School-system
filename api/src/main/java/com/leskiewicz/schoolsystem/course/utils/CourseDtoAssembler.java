package com.leskiewicz.schoolsystem.course.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.course.CourseController;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.user.UserController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CourseDtoAssembler extends RepresentationModelAssemblerSupport<CourseDto, CourseDto> {

  public CourseDtoAssembler() {
    super(CourseController.class, CourseDto.class);
  }

  @Override
  public CourseDto toModel(CourseDto course) {
    Link selfLink =
        linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel();
    Link facultyLink =
        linkTo(methodOn(FacultyController.class).getFacultyById(course.getFacultyId()))
            .withRel("faculty");
    Link teacherLink =
        linkTo(methodOn(UserController.class).getUserById(course.getTeacherId()))
            .withRel("teacher");
    Link studentsLink =
        linkTo(methodOn(CourseController.class).getCourseStudents(course.getId(), null))
            .withRel("students");

    course.add(selfLink);
    course.add(facultyLink);
    course.add(teacherLink);
    course.add(studentsLink);

    return course;
  }
}
