package com.leskiewicz.schoolsystem.security;

import com.leskiewicz.schoolsystem.security.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @Valid @RequestBody RegisterRequest request) {

    logger.info("Received request to register new user: {}",
        StringUtils.maskEmail(request.getEmail()));
    AuthenticationResponse response = authenticationService.register(request);
    logger.info("User: {} successfully registered", StringUtils.maskEmail(request.getEmail()));
    registrationAddLinks(request, response);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @Valid @RequestBody AuthenticationRequest request) {

    logger.info("Received request to authenticate user: {}",
        StringUtils.maskEmail(request.getEmail()));
    AuthenticationResponse response = authenticationService.authenticate(request);
    authenticationAddLinks(request, response);
    logger.info("Successfully authenticated user: {}", StringUtils.maskEmail(request.getEmail()));

    return ResponseEntity.ok(response);
  }

  private void registrationAddLinks(RegisterRequest request, AuthenticationResponse response) {
    // Create links
    Link selfLink = WebMvcLinkBuilder.linkTo(
        methodOn(AuthenticationController.class).register(request)).withSelfRel();
    Link authenticateLink = WebMvcLinkBuilder.linkTo(
        methodOn(AuthenticationController.class).authenticate(null)).withRel("authenticate");
    response.add(selfLink);
    response.add(authenticateLink);
  }

  private void authenticationAddLinks(AuthenticationRequest request,
      AuthenticationResponse response) {
    // Create links
    Link selfLink = WebMvcLinkBuilder.linkTo(
        methodOn(AuthenticationController.class).authenticate(request)).withSelfRel();
    Link authenticateLink = WebMvcLinkBuilder.linkTo(
        methodOn(AuthenticationController.class).register(null)).withRel("register");
    response.add(selfLink);
    response.add(authenticateLink);
  }
}
