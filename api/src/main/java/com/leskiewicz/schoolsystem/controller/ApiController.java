package com.leskiewicz.schoolsystem.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping
    public ResponseEntity<RepresentationModel<?>> index() {
        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(linkTo(methodOn(ApiController.class).index()).withSelfRel());

        // Authentication
//        model.add(linkTo(methodOn(AuthenticationController.class).authenticate(null)).withRel("authenticate"));
//        model.add(linkTo(methodOn(AuthenticationController.class).register(null)).withRel("register"));

        // Users


        return ResponseEntity.ok(model);
    }
}
