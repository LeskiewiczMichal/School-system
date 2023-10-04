package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.article.ArticleCategory;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.MessageModel;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.APIResponses;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing {@link Degree}.
 *
 * <p>All endpoints return responses formatted as HAL representations with _links. Collections are
 * return inside _embedded field.
 */
@RestController
@RequestMapping("/api/degrees")
@AllArgsConstructor
public class DegreeController {

  private final DegreeService degreeService;

  // Used to convert DTOs to HAL representations
  private final DegreeDtoAssembler degreeDtoAssembler;
  private final CourseDtoAssembler courseDtoAssembler;

  // Used to add links to paged resources
  private final PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  private final PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  /**
   * Get a degree by its ID.
   *
   * @param id the ID of the degree to retrieve.
   * @return status 200 and the {@link DegreeDto} representing the degree with the given ID in the
   *     body.
   * @throws EntityNotFoundException if the degree does not exist, returns status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}")
  public ResponseEntity<DegreeDto> getDegreeById(@PathVariable Long id) {
    DegreeDto degree = degreeService.getById(id);
    degree = degreeDtoAssembler.toModel(degree);

    return ResponseEntity.ok(degree);
  }

  /**
   * Get all degrees.
   *
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link DegreeDto} objects and page
   *     metadata. If there are no degrees, an empty page is returned (without _embedded.degrees
   *     field).
   */
  @GetMapping
  public ResponseEntity<RepresentationModel<DegreeDto>> getDegrees(
      @ModelAttribute PageableRequest request) {
    Page<DegreeDto> degrees = degreeService.getDegrees(request.toPageable());
    degrees = degreeDtoAssembler.mapPageToModel(degrees);

    Link selfLink =
        linkTo(methodOn(this.getClass()).getDegrees(null))
            .withSelfRel();

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(degreePagedResourcesAssembler.toModel(degrees))
            .links(List.of(selfLink, retrieveGetByIdLink(), retrieveSearchLink()))
            .build());
  }

  /**
   * Creates a new degree based on the given request.
   *
   * @param request The {@link CreateDegreeRequest} containing the data to create the degree.
   * @return status 201 with created {@link DegreeDto} in the body.
   * @throws EntityAlreadyExistsException and returns status 400 if a degree with the same title,
   *     field of study, and faculty already exists.
   * @throws EntityNotFoundException and returns status 404 if provided faculty does not exist.
   * @throws MethodArgumentNotValidException, returns status 400 and body with path, message about
   *     missing fields, statusCode if the request is invalid.
   * @throws AccessDeniedException, returns status 403 if the user is not authorized to create a
   *     degree.
   */
  @PostMapping
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
  public ResponseEntity<DegreeDto> createDegree(@Valid @RequestBody CreateDegreeRequest request) {
    DegreeDto degree = degreeService.createDegree(request);
    degree = degreeDtoAssembler.toModel(degree);

    return ResponseEntity.created(degree.getLink("self").get().toUri()).body(degree);
  }

  /**
   * Get all courses of degree with provided ID.
   *
   * @param id the ID of the degree to retrieve courses from.
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link CourseDto} objects and page
   *     metadata. If there are no courses, an empty page is returned (without _embedded.courses
   *     field).
   * @throws MethodArgumentNotValidException, returns status 400 and body with path, message about
   *     missing fields, statusCode if the request is invalid.
   */
  @GetMapping("/{id}/courses")
  public ResponseEntity<RepresentationModel<CourseDto>> getDegreeCourses(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<CourseDto> courses = degreeService.getDegreeCourses(id, request.toPageable());
    courses = courses.map(courseDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(coursePagedResourcesAssembler.toModel(courses)).build());
  }

  /**
   * Search degrees
   *
   * @param fieldOfStudy (optional, param name "fieldOfStudy") field of study to search by.
   * @param facultyId (optional, param name "faculty") ID of the faculty to search by.
   * @param title (optional, param name "degreeTitle") {@link DegreeTitle} title of the degree to
   *     search by.
   * @param request ${@link PageableRequest} with pagination parameters.
   * @return status 200 (OK) and in body the paged list of {@link DegreeDto} objects and page
   *     metadata. If there are no articles, an empty page is returned (without _embedded.degrees
   *     field).
   */
  @GetMapping("/search")
  public ResponseEntity<RepresentationModel<DegreeDto>> searchDegrees(
      @RequestParam(value = "fieldOfStudy", required = false) String fieldOfStudy,
      @RequestParam(value = "faculty", required = false) Long facultyId,
      @RequestParam(value = "degreeTitle", required = false) DegreeTitle title,
      @ModelAttribute PageableRequest request) {
    Page<DegreeDto> degrees =
        degreeService.search(fieldOfStudy, facultyId, title, request.toPageable());
    degrees = degreeDtoAssembler.mapPageToModel(degrees);

    Link selfLink =
        linkTo(
                methodOn(this.getClass()).searchDegrees(null, null, null, null))
            .withSelfRel();

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(degreePagedResourcesAssembler.toModel(degrees))
            .links(List.of(selfLink, retrieveGetDegreesLink(), retrieveGetByIdLink()))
            .build());
  }

  /**
   * Update image in degree with provided ID.
   *
   * @param id the ID of the degree to update image in.
   * @param file the image file to upload.
   * @return status 200 (OK) and in body the message about successful upload.
   */
  @PostMapping("/{id}/image")
  public ResponseEntity<MessageModel> updateImage(
      @PathVariable Long id, @RequestParam("file") MultipartFile file) {
    degreeService.addImage(id, file);
    MessageModel message = createImageUpdatedMessage(id, file);

    return ResponseEntity.status(HttpStatus.OK).body(message);
  }

  private MessageModel createImageUpdatedMessage(Long id, MultipartFile file) {
    MessageModel message =
        new MessageModel(APIResponses.fileUploaded(file.getOriginalFilename()));
    message.add(linkTo(methodOn(this.getClass()).getDegreeById(id)).withRel("degree"));

    return message;
  }

  private Link retrieveGetDegreesLink() {
    return linkTo(methodOn(this.getClass()).getDegrees(null)).withRel("degrees");
  }

  private Link retrieveGetByIdLink() {
    return linkTo(methodOn(this.getClass()).getDegreeById(null)).withRel("degree");
  }

  private Link retrieveSearchLink() {
    return linkTo(methodOn(this.getClass()).searchDegrees(null, null, null, null)).withRel("degrees");
  }
}
