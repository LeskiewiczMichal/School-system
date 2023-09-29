package com.leskiewicz.schoolsystem.degree.utils;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DegreeDtoAssembler extends RepresentationModelAssemblerSupport<DegreeDto, DegreeDto> {

  public DegreeDtoAssembler() {
    super(DegreeController.class, DegreeDto.class);
  }

  @Override
  public DegreeDto toModel(DegreeDto degree) {
    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(DegreeController.class).getDegreeById(degree.getId()))
            .withSelfRel();
    Link facultyLink =
        WebMvcLinkBuilder.linkTo(
                methodOn(FacultyController.class).getFacultyById(degree.getFacultyId()))
            .withRel("faculty");
    Link coursesLink =
        WebMvcLinkBuilder.linkTo(
                methodOn(DegreeController.class).getDegreeCourses(degree.getId(), null))
            .withRel("courses");

    degree.add(selfLink);
    degree.add(facultyLink);
    degree.add(coursesLink);

    return degree;
  }

  public Page<DegreeDto> mapPageToModel(Page<DegreeDto> degrees) {
    degrees.forEach(this::toModel);
    return degrees;
  }
}
