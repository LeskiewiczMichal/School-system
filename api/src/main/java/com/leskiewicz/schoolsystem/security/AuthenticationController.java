package com.leskiewicz.schoolsystem.security;

import com.leskiewicz.schoolsystem.security.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthenticationResponse response = authenticationService.register(request);

//        Create links
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(AuthenticationController.class).register(request)).withSelfRel();
        Link authenticateLink = WebMvcLinkBuilder.linkTo(methodOn(AuthenticationController.class).authenticate(null)).withRel("authenticate");
        response.add(selfLink);
        response.add(authenticateLink);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {

        AuthenticationResponse response = authenticationService.authenticate(request);

//        Create links
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(AuthenticationController.class).authenticate(request)).withSelfRel();
        Link authenticateLink = WebMvcLinkBuilder.linkTo(methodOn(AuthenticationController.class).register(null)).withRel("register");
        response.add(selfLink);
        response.add(authenticateLink);

        return ResponseEntity.ok(response);
    }
}
