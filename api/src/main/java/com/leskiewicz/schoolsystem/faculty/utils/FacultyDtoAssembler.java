package com.leskiewicz.schoolsystem.faculty.utils;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FacultyDtoAssembler extends RepresentationModelAssemblerSupport<FacultyDto, FacultyDto> {

    public FacultyDtoAssembler() {
        super(FacultyController.class, FacultyDto.class);
    }

    @Override
  public FacultyDto toModel(FacultyDto faculty) {
        Link selfLink =
                WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyById(faculty.getId()))
                        .withSelfRel();

        Link studentsLink =
                WebMvcLinkBuilder.linkTo(
                                methodOn(FacultyController.class).getFacultyStudents(faculty.getId(), null))
                        .withRel("students");
        Link teachersLink =
                WebMvcLinkBuilder.linkTo(
                                methodOn(FacultyController.class).getFacultyTeachers(faculty.getId(), null))
                        .withRel("teachers");

        faculty.add(selfLink);
        faculty.add(studentsLink);
        faculty.add(teachersLink);

        return faculty;
    }
}
