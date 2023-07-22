package com.leskiewicz.schoolsystem.security;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.security.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final UserDtoAssembler userDtoAssembler;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @Valid @RequestBody RegisterRequest request) {

    AuthenticationResponse response = authenticationService.register(request);
    response.setUser(userDtoAssembler.toModel(response.getUser()));
    registrationAddLinks(request, response);

    return ResponseEntity.created(response.getUser().getLink("self").get().toUri()).body(response);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @Valid @RequestBody AuthenticationRequest request) {

    AuthenticationResponse response = authenticationService.authenticate(request);
    response.setUser(userDtoAssembler.toModel(response.getUser()));
    authenticationAddLinks(request, response);

    return ResponseEntity.ok(response);
  }

  private void registrationAddLinks(RegisterRequest request, AuthenticationResponse response) {
    // Create links
    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(AuthenticationController.class).register(request))
            .withSelfRel();
    Link authenticateLink =
        WebMvcLinkBuilder.linkTo(methodOn(AuthenticationController.class).authenticate(null))
            .withRel("authenticate");
    response.add(selfLink);
    response.add(authenticateLink);
  }

  private void authenticationAddLinks(
      AuthenticationRequest request, AuthenticationResponse response) {
    // Create links
    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(AuthenticationController.class).authenticate(request))
            .withSelfRel();
    Link authenticateLink =
        WebMvcLinkBuilder.linkTo(methodOn(AuthenticationController.class).register(null))
            .withRel("register");
    response.add(selfLink);
    response.add(authenticateLink);
  }
}
