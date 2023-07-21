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
public class FacultyModelAssembler
    extends RepresentationModelAssemblerSupport<Faculty, FacultyDto> {

  private final FacultyMapper facultyMapper;

  public FacultyModelAssembler(FacultyMapper facultyMapper) {
    super(FacultyController.class, FacultyDto.class);
    this.facultyMapper = facultyMapper;
  }

  @Override
  public FacultyDto toModel(Faculty entity) {
    FacultyDto facultyDto = facultyMapper.convertToDto(entity);

    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyById(entity.getId()))
            .withSelfRel();

    Link studentsLink =
        WebMvcLinkBuilder.linkTo(
                methodOn(FacultyController.class).getFacultyStudents(entity.getId(), null))
            .withRel("students");
    Link teachersLink =
        WebMvcLinkBuilder.linkTo(
                methodOn(FacultyController.class).getFacultyTeachers(entity.getId(), null))
            .withRel("teachers");

    facultyDto.add(selfLink);
    facultyDto.add(studentsLink);
    facultyDto.add(teachersLink);

    return facultyDto;
  }

  @Override
  public CollectionModel<FacultyDto> toCollectionModel(Iterable<? extends Faculty> entities) {

    return super.toCollectionModel(entities);
  }
}
