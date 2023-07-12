package com.leskiewicz.schoolsystem.controller;

import com.leskiewicz.schoolsystem.dto.request.AuthenticationRequest;
import com.leskiewicz.schoolsystem.dto.request.RegisterRequest;
import com.leskiewicz.schoolsystem.dto.response.AuthenticationResponse;
import com.leskiewicz.schoolsystem.error.MissingFieldException;
import com.leskiewicz.schoolsystem.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {
//        try {
            AuthenticationResponse response = authenticationService.register(request);
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).register(request)).withSelfRel();
            response.add(selfLink);
            return ResponseEntity.ok(response);
//        } catch (MissingFieldException ex) {
//            throw new
//        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticate(request)).withSelfRel();
        response.add(selfLink);
        return ResponseEntity.ok(response);
    }
}
