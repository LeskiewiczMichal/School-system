package com.leskiewicz.schoolsystem.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.article.ArticleController;
import com.leskiewicz.schoolsystem.authentication.AuthenticationController;
import com.leskiewicz.schoolsystem.authentication.SecurityService;
import com.leskiewicz.schoolsystem.authentication.dto.CustomUserDetails;
import com.leskiewicz.schoolsystem.course.CourseController;
import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.files.FileController;
import com.leskiewicz.schoolsystem.user.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

  @Autowired private SecurityService securityService;
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
    this.addFilesLink(model);
    this.addArticlesLink(model);

    return ResponseEntity.ok(model);
  }

  private void addAuthenticationLinks(RepresentationModel<?> model) {
    model.add(
        linkTo(methodOn(AuthenticationController.class).authenticate(null))
            .withRel("authenticate"));
    model.add(linkTo(methodOn(AuthenticationController.class).register(null)).withRel("register"));
    model.add(
        linkTo(methodOn(AuthenticationController.class).authenticateWithToken(null))
            .withRel("authenticateWithToken"));
  }

  private void addUsersLinks(RepresentationModel<?> model) {
    Link usersLink = linkTo(UserController.class).withRel("users");
    String uriTemplate = usersLink.getHref() + PAGINATION_PARAMETERS;
    usersLink = Link.of(uriTemplate).withRel("users");

    Link getByIdLink =
        linkTo(methodOn(UserController.class).getUserById(null))
            .withRel("users")
            .withTitle("Get by id");
    Link searchLink =
        linkTo(methodOn(UserController.class).searchUsers(null, null, null, null))
            .withRel("users")
            .withTitle("Search");

    model.add(usersLink);
    model.add(getByIdLink);
    model.add(searchLink);
  }

  private void addFacultiesLinks(RepresentationModel<?> model) {
    Link facultiesLink = linkTo(FacultyController.class).withRel("faculties");
    String uriTemplate = facultiesLink.getHref() + PAGINATION_PARAMETERS;
    facultiesLink = Link.of(uriTemplate).withRel("faculties");

    Link getByIdLink =
        linkTo(methodOn(FacultyController.class).getFacultyById(null)).withRel("faculties");

    model.add(facultiesLink);
    model.add(getByIdLink);
  }

  private void addDegreesLink(RepresentationModel<?> model) {
    Link degreesLink = linkTo(DegreeController.class).withRel("degrees");
    String uriTemplate = degreesLink.getHref() + PAGINATION_PARAMETERS;
    degreesLink = Link.of(uriTemplate).withRel("degrees");

    Link getByIdLink =
        linkTo(methodOn(DegreeController.class).getDegreeById(null))
            .withRel("degrees")
            .withTitle("Get by id");
    Link searchLink =
        linkTo(methodOn(DegreeController.class).searchDegrees(null, null, null, null))
            .withRel("degrees")
            .withTitle("Search");

    model.add(degreesLink);
    model.add(getByIdLink);
    model.add(searchLink);
  }

  private void addCoursesLink(RepresentationModel<?> model) {
    Link coursesLink = linkTo(CourseController.class).withRel("courses");
    String uriTemplate = coursesLink.getHref() + PAGINATION_PARAMETERS;
    coursesLink = Link.of(uriTemplate).withRel("courses");

    Link getByIdLink =
        linkTo(methodOn(CourseController.class).getCourseById(null))
            .withRel("courses")
            .withTitle("Get by id");
    Link searchLink =
        linkTo(methodOn(CourseController.class).searchCourses(null, null, null, null))
            .withRel("courses")
            .withTitle("Search");

    model.add(coursesLink);
    model.add(getByIdLink);
    model.add(searchLink);
  }

  private void addFilesLink(RepresentationModel<?> model) {
    Link filesLink = linkTo(FileController.class).withRel("files");
    String uriTemplate = filesLink.getHref() + PAGINATION_PARAMETERS;
    filesLink = Link.of(uriTemplate).withRel("files");
    model.add(filesLink);
  }

  private void addArticlesLink(RepresentationModel<?> model) {
    Link articlesLink = linkTo(ArticleController.class).withRel("articles");
    String uriTemplate = articlesLink.getHref() + PAGINATION_PARAMETERS;
    articlesLink = Link.of(uriTemplate).withRel("articles");

    Link getByIdLink =
        linkTo(methodOn(ArticleController.class).getArticleById(null))
            .withRel("articles")
            .withTitle("Get by id");

    Link searchLink =
        linkTo(methodOn(ArticleController.class).searchArticles(null, null, null))
            .withRel("articles")
            .withTitle("Search");

    model.add(articlesLink);
    model.add(getByIdLink);
    model.add(searchLink);
  }
}
