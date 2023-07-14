package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.controller.AuthenticationController;
import com.leskiewicz.schoolsystem.model.Faculty;
import com.leskiewicz.schoolsystem.model.User;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@AllArgsConstructor
public class LinksServiceImpl implements LinksService {

    private final EntityLinks entityLinks;

    public void addLinks(User user) {
        Link selfLink = entityLinks.linkToItemResource(User.class, user.getId()).withSelfRel();
        Link facultyLink = entityLinks.linkToItemResource(Faculty.class, user.getFaculty().getId()).withRel("faculty");

        user.add(selfLink);
        user.add(facultyLink);
    }
}
