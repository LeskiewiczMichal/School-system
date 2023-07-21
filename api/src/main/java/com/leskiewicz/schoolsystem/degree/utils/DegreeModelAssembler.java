package com.leskiewicz.schoolsystem.degree.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class DegreeModelAssembler extends RepresentationModelAssemblerSupport<Degree, DegreeDto> {

  private final DegreeMapper degreeMapper;

  public DegreeModelAssembler(DegreeMapper degreeMapper) {
    super(DegreeController.class, DegreeDto.class);
    this.degreeMapper = degreeMapper;
  }

  @Override
  public DegreeDto toModel(Degree entity) {
    DegreeDto degreeDto = degreeMapper.convertToDto(entity);

    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(DegreeController.class).getDegreeById(degreeDto.getId()))
            .withSelfRel();
    Link facultyLink =
        WebMvcLinkBuilder.linkTo(
                methodOn(FacultyController.class).getFacultyById(entity.getFaculty().getId()))
            .withRel("faculty");

    degreeDto.add(selfLink);
    degreeDto.add(facultyLink);

    return degreeDto;
  }

  @Override
  public CollectionModel<DegreeDto> toCollectionModel(Iterable<? extends Degree> entites) {

    return super.toCollectionModel(entites);
  }
}
