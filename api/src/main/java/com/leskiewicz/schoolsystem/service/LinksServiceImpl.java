package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.controller.UserController;
import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@AllArgsConstructor
public class LinksServiceImpl implements LinksService {

    public void addLinks(UserDto userDto) {
//        Link selfLink = entityLinks.linkToItemResource(User.class, userDto.getId()).withSelfRel();
//        Link selfLink = WebMvcLinkBuilder
//                .linkTo(methodOn(UserController.class)
////                .getUsers(0, 10, "id", null))
//                        .getUsers(PageableRequest.builder()
//                                .page(0)
//                                .size(10)
//                                .sort("id")
//                                .direction("ASC")
//                                .build()))
//                .withSelfRel();
////        Link facultyLink = entityLinks.linkToItemResource(Faculty.class, user.getFaculty().getId()).withRel("faculty");
//
//        userDto.add(selfLink);
//        userDto.add(facultyLink);
    }
}
