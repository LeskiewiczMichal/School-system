package com.leskiewicz.schoolsystem.degree;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@ExtendWith(MockitoExtension.class)
public class DegreeDtoAssemblerTest {

  // Variables
  DegreeDto degreeDto;
  @InjectMocks private DegreeDtoAssembler degreeDtoAssembler;

  @BeforeEach
  public void setup() {
    degreeDto =
        DegreeDto.builder()
            .id(0L)
            .fieldOfStudy("Computer Science")
            .title(DegreeTitle.BACHELOR)
            .faculty("Faculty")
            .facultyId(1L)
            .build();
  }

  @Test
  public void testToModelAddsCorrectLinks() {
    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(DegreeController.class).getDegreeById(0L)).withSelfRel();
    Link facultyLink =
        WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyById(1L))
            .withRel("faculty");

    DegreeDto result = degreeDtoAssembler.toModel(degreeDto);

    Assertions.assertEquals(selfLink, result.getLink("self").get());
    Assertions.assertEquals(facultyLink, result.getLink("faculty").get());
  }
}
