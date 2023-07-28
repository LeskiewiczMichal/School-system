package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.controller.ApiController;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.dto.request.MessageModel;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.customexception.DuplicateEntityException;
import com.leskiewicz.schoolsystem.error.customexception.EntitiesAlreadyAssociatedException;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.user.UserController;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing courses.
 *
 * <p>All endpoints return responses formatted as HAL representations with _links. Collections are
 * return inside _embedded field.
 */
@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {

  private final CourseService courseService;

  // Used to convert DTOs to HAL representations
  private final CourseDtoAssembler courseDtoAssembler;
  private final UserDtoAssembler userDtoAssembler;

  // Used to add links to paged resources
  private final PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;
  private final PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;

  /**
   * Get a course by its ID.
   *
   * @param id the ID of the course to retrieve.
   * @return status 200 and the {@link CourseDto} representing the course with the given ID in the
   *     body.
   * @throws EntityNotFoundException if the course does not exist, returns status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   */
  @GetMapping("/{id}")
  public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
    CourseDto course = courseService.getById(id);
    course = courseDtoAssembler.toModel(course);

    return ResponseEntity.ok(course);
  }

  /**
   * Get all courses.
   *
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link CourseDto} objects and page
   *     metadata. If there are no courses, an empty page is returned (without _embedded.faculties
   *     field).
   */
  @GetMapping
  public ResponseEntity<RepresentationModel<CourseDto>> getCourses(
      @ModelAttribute PageableRequest request) {
    Page<CourseDto> courses = courseService.getCourses(request.toPageable());
    courses = courses.map(courseDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(coursePagedResourcesAssembler.toModel(courses)).build());
  }

  /**
   * Create a new course.
   *
   * @param request the {@link CreateCourseRequest} containing the course data.
   * @return status 201 (Created) and in body the {@link CourseDto} representing the created course
   * @throws EntityNotFoundException if the faculty or teacher does not exist, returns status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   * @throws MethodArgumentNotValidException, returns status 400 and body with path, message about
   *     missing fields, statusCode if the request is invalid.
   * @throws EntityAlreadyExistsException if the course already exists, returns status 400.
   * @throws AccessDeniedException if the user is not authorized to create a course, returns status
   *     403
   */
  @PostMapping
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
  public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CreateCourseRequest request) {
    CourseDto course = courseService.createCourse(request);
    course = courseDtoAssembler.toModel(course);

    return ResponseEntity.created(course.getLink("self").get().toUri()).body(course);
  }

  /**
   * Get all students of course with provided ID.
   *
   * @param id the ID of the faculty to retrieve degrees from.
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link UserDto} objects and page
   *     metadata. If there are no students, an empty page is returned (without _embedded.users
   *     field).
   */
  @GetMapping("/{id}/students")
  public ResponseEntity<RepresentationModel<UserDto>> getCourseStudents(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<UserDto> students = courseService.getCourseStudents(id, request.toPageable());
    students = students.map(userDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(students)).build());
  }

  /**
   * Add a student to a course.
   *
   * @param id the ID of the course to add the student to.
   * @param userId the ID of the student to add to the course.
   * @return status 200 (OK) and in body a {@link MessageModel} with a message about the operation
   *     and links to the course and the student.
   * @throws EntityNotFoundException if the course or student does not exist, returns status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   * @throws EntitiesAlreadyAssociatedException if the student is already enrolled in the course,
   *     returns status 400.
   */
  @PostMapping("/{id}/students")
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<MessageModel> addStudentToCourse(
      @PathVariable Long id, @RequestBody Long userId) {
    courseService.addStudentToCourse(userId, id);

    MessageModel message = new MessageModel("Student added to course successfully");
    message.add(
        WebMvcLinkBuilder.linkTo(methodOn(CourseController.class).getCourseById(id))
            .withRel("course"));
    message.add(
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(userId))
            .withRel("student"));

    return ResponseEntity.status(HttpStatus.OK).body(message);
  }

  @DeleteMapping("/{id}")
  //  @PreAuthorize() //TODO: teacher that is teaching the course or admin
  public ResponseEntity<MessageModel> deleteCourseById(@PathVariable Long id) {
    courseService.deleteCourse(id);

    MessageModel message = new MessageModel("Course deleted successfully");
    message.add(WebMvcLinkBuilder.linkTo(ApiController.class).withRel("api"));
    message.add(WebMvcLinkBuilder.linkTo(CourseController.class).withRel("courses"));

    return ResponseEntity.ok(message);
  }
}
