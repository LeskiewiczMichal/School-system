package com.leskiewicz.schoolsystem.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.authentication.AuthenticationController;
import com.leskiewicz.schoolsystem.authentication.dto.CustomUserDetails;
import com.leskiewicz.schoolsystem.course.CourseController;
import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.user.UserController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

  private final String PAGINATION_PARAMETERS = "{?size,page,sort}";

  /// ******************** THIS IS HOW TO GET USER DETAILS *********************** ///
//    @GetMapping("/authenticationUser")
//    public ResponseEntity<String> getAuthentication(Authentication authentication) {
//      CustomUserDetails customUserDetails = (CustomUserDetails) (authentication.getPrincipal());
//      System.out.println(customUserDetails.());
//      return ResponseEntity.ok(authentication.toString());
//    }

  @GetMapping
  public ResponseEntity<RepresentationModel<?>> index() {
    RepresentationModel<?> model = new RepresentationModel<>();

    // Adding links to base api path
    model.add(linkTo(methodOn(ApiController.class).index()).withSelfRel());
    this.addAuthenticationLinks(model);
    this.addUsersLinks(model);
    this.addFacultiesLinks(model);
    this.addDegreesLink(model);
    this.addCoursesLink(model);

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

  private void addDegreesLink(RepresentationModel<?> model) {
    Link degreesLink = linkTo(DegreeController.class).withRel("degrees");
    String uriTemplate = degreesLink.getHref() + PAGINATION_PARAMETERS;
    degreesLink = Link.of(uriTemplate).withRel("degrees");
    model.add(degreesLink);
  }

  private void addCoursesLink(RepresentationModel<?> model) {
    Link coursesLink = linkTo(CourseController.class).withRel("courses");
    String uriTemplate = coursesLink.getHref() + PAGINATION_PARAMETERS;
    coursesLink = Link.of(uriTemplate).withRel("courses");
    model.add(coursesLink);
  }
}
