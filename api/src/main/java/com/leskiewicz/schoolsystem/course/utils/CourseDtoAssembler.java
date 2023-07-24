package com.leskiewicz.schoolsystem.course.utils;

import com.leskiewicz.schoolsystem.course.CourseController;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CourseDtoAssembler extends RepresentationModelAssemblerSupport<CourseDto, CourseDto> {

    public CourseDtoAssembler() {
        super(CourseController.class, CourseDto.class);
    }

    @Override
    public CourseDto toModel(CourseDto course) {
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel();

        course.add(selfLink);

        return course;
    }
}
