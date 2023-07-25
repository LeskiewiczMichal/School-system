package com.leskiewicz.schoolsystem.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.authentication.AuthenticationController;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.user.UserController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
  //        Link selfLink = linkTo(controllerClass).withSelfRel();
  //        String uriTemplate = selfLink.getHref() + "{?size,page,sort,direction}";
  //        selfLink = Link.of(uriTemplate).withSelfRel();
  //        resources.add(selfLink);
  private String PAGINATION_PARAMETERS = "{?size,page,sort}";

  @GetMapping
  public ResponseEntity<RepresentationModel<?>> index() {
    RepresentationModel<?> model = new RepresentationModel<>();

    model.add(linkTo(methodOn(ApiController.class).index()).withSelfRel());

    this.addAuthenticationLinks(model);
    this.addUsersLinks(model);
    this.addFacultiesLinks(model);

    return ResponseEntity.ok(model);
  }

  private void addAuthenticationLinks(RepresentationModel<?> model) {

    model.add(
        linkTo(methodOn(AuthenticationController.class).authenticate(null))
            .withRel("authenticate"));
    model.add(linkTo(methodOn(AuthenticationController.class).register(null)).withRel("register"));
  }

  private void addUsersLinks(RepresentationModel<?> model) {
    Link usersLink = linkTo(UserController.class).withRel("users");
    String uriTemplate = usersLink.getHref() + PAGINATION_PARAMETERS;
    usersLink = Link.of(uriTemplate).withRel("users");
    model.add(usersLink);
  }

  private void addFacultiesLinks(RepresentationModel<?> model) {
    Link facultiesLink = linkTo(FacultyController.class).withRel("faculties");
    String uriTemplate = facultiesLink.getHref() + PAGINATION_PARAMETERS;
    facultiesLink = Link.of(uriTemplate).withRel("faculties");
    model.add(facultiesLink);
  }

  
}
