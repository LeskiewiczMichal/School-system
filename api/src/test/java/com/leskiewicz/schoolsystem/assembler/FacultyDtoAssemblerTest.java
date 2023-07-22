package com.leskiewicz.schoolsystem.assembler;

import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyDtoAssembler;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class FacultyDtoAssemblerTest {

  // Variables
  FacultyDto faculty;
  @InjectMocks private FacultyDtoAssembler facultyDtoAssembler;

  @BeforeEach
  public void setup() {

    faculty = FacultyDto.builder().id(0L).name("Computer Science").build();
  }

  @Test
  public void testToModelAddsCorrectLinks() {
    Link selfLink =
            WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyById(0L))
                    .withSelfRel();
    Link studentsLink =
            WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyStudents(0L, null))
                    .withRel("students");
    Link teachersLink =
            WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyTeachers(0L, null))
                    .withRel("teachers");

    com.leskiewicz.schoolsystem.faculty.dto.FacultyDto result = facultyDtoAssembler.toModel(faculty);

    Assertions.assertEquals(selfLink, result.getLink("self").get());
    Assertions.assertEquals(studentsLink, result.getLink("students").get());
    Assertions.assertEquals(teachersLink, result.getLink("teachers").get());
  }
}
