package com.leskiewicz.schoolsystem.faculty.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class FacultyModelAssembler extends
    RepresentationModelAssemblerSupport<Faculty, FacultyDto> {


  public FacultyModelAssembler() {
    super(FacultyController.class, FacultyDto.class);
  }

  @Override
  public FacultyDto toModel(Faculty entity) {
    FacultyDto facultyDto = FacultyMapper.convertToDto(entity);

    Link selfLink = WebMvcLinkBuilder.linkTo(
        methodOn(FacultyController.class).getFacultyById(entity.getId())).withSelfRel();
    facultyDto.add(selfLink);

    return facultyDto;
  }

  @Override
  public CollectionModel<FacultyDto> toCollectionModel(Iterable<? extends Faculty> entities) {

    return super.toCollectionModel(entities);
  }
}
