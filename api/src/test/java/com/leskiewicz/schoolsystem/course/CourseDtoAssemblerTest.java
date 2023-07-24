package com.leskiewicz.schoolsystem.course;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@ExtendWith(MockitoExtension.class)
public class CourseDtoAssemblerTest {

  // Variables
  CourseDto course;
  @InjectMocks private CourseDtoAssembler courseDtoAssembler;

  @BeforeEach
  public void setup() {
    course = TestHelper.createCourseDto("Computer Science", "Bob Dylan");
  }

  @Test
  public void testToModelAddsCorrectLinks() {
    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(CourseController.class).getCourseById(1L)).withSelfRel();
    Link facultyLink =
        WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyById(1L))
            .withRel("faculty");
    Link teacherLink =
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(course.getTeacherId()))
            .withRel("teacher");

    CourseDto result = courseDtoAssembler.toModel(course);

    Assertions.assertEquals(selfLink, result.getLink("self").get());
    Assertions.assertEquals(facultyLink, result.getLink("faculty").get());
    Assertions.assertEquals(teacherLink, result.getLink("teacher").get());
  }
}
