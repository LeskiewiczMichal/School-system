package com.leskiewicz.schoolsystem.authentication;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication.
 *
 * <p>All endpoints return responses formatted as HAL representations with _links.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  // Used to covert DTOs to HAL representations
  private final UserDtoAssembler userDtoAssembler;

  /**
   * Creates a new user based on the given request.
   *
   * @param request The request containing the data to create the user.
   * @return status 201 with created UserDto and authentication token in the body.
   * @throws EntityAlreadyExistsException and returns status 400 if user with the same email
   *     already exists.
   * @throws MethodArgumentNotValidException and returns status 400 if the request is invalid.
   */
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
