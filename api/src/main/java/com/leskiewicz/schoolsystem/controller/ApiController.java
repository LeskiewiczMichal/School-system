package com.leskiewicz.schoolsystem.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.authentication.AuthenticationController;
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
  @GetMapping
  public ResponseEntity<RepresentationModel<?>> index() {
    RepresentationModel<?> model = new RepresentationModel<>();

    model.add(linkTo(methodOn(ApiController.class).index()).withSelfRel());

    this.addAuthenticationLinks(model);
    this.addUsersLinks(model);

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
    String uriTemplate = usersLink.getHref() + "{?size,page,sort,direction}";
    usersLink = Link.of(uriTemplate).withRel("users");
    model.add(usersLink);
  }
}
