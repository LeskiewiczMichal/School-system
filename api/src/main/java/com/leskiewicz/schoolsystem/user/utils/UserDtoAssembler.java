package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserController;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDtoAssembler extends RepresentationModelAssemblerSupport<UserDto, UserDto> {


    public UserDtoAssembler(Class<?> controllerClass, Class<UserDto> resourceType) {
        super(UserController.class, UserDto.class);
    }

    @Override
    public UserDto toModel(UserDto user) {

        Link selfLink =
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(user.getId()))
                        .withSelfRel();
        Link facultyLink =
                WebMvcLinkBuilder.linkTo(
                                methodOn(FacultyController.class).getFacultyById(user.getId()))
                        .withRel("faculty");

        user.add(selfLink);
        user.add(facultyLink);

        return user;
    }
}
