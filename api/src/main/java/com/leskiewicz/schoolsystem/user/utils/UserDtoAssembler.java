package com.leskiewicz.schoolsystem.user.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.course.CourseController;
import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.user.UserController;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserDtoAssembler extends RepresentationModelAssemblerSupport<UserDto, UserDto> {

  public UserDtoAssembler() {
    super(UserController.class, UserDto.class);
  }

  @Override
  public UserDto toModel(UserDto user) {

    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(user.getId()))
            .withSelfRel();
    Link facultyLink =
        WebMvcLinkBuilder.linkTo(
                methodOn(FacultyController.class).getFacultyById(user.getFacultyId()))
            .withRel("faculty");
    Link coursesLink =
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserCourses(user.getId(), null))
            .withRel("courses");

    user.add(selfLink);
    user.add(facultyLink);
    user.add(coursesLink);

    // Degree may be empty if user is not a student
    if (user.getDegreeId() != null) {
      Link degreeLink =
          WebMvcLinkBuilder.linkTo(
                  methodOn(DegreeController.class).getDegreeById(user.getDegreeId()))
              .withRel("degree");
      user.add(degreeLink);
    }

    // TODO: add link to retrieve teacher details
//    if (user.getTeacherDetailsId() != null) {
//      Link teacherDetailsLink =
//          WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getTeacherDetails(user.getId()))
//              .withRel("teacherDetails");
//    }

    return user;
  }
}
