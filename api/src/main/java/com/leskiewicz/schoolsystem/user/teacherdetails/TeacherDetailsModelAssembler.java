package com.leskiewicz.schoolsystem.user.teacherdetails;

import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TeacherDetailsModelAssembler
    extends RepresentationModelAssemblerSupport<TeacherDetails, TeacherDetails> {
  public TeacherDetailsModelAssembler() {
    super(UserController.class, TeacherDetails.class);
  }

  @Override
  public TeacherDetails toModel(TeacherDetails teacherDetails) {

    User teacher = teacherDetails.getTeacher();

    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getTeacherDetails(teacher.getId()))
            .withSelfRel();
    Link teacherLink =
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(teacher.getId()))
            .withRel("teacher");

    teacherDetails.add(selfLink);
    teacherDetails.add(teacherLink);

    return teacherDetails;
  }
}
