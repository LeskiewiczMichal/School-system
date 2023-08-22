package com.leskiewicz.schoolsystem.course;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.authentication.SecurityService;
import com.leskiewicz.schoolsystem.authentication.dto.CustomUserDetails;
import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.controller.ApiController;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.MessageModel;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.APIResponses;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.EntitiesAlreadyAssociatedException;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.error.customexception.FileUploadFailedException;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.files.FileModelAssembler;
import com.leskiewicz.schoolsystem.user.UserController;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import com.leskiewicz.schoolsystem.utils.Language;
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
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
  private final SecurityService securityService;

  // Used to convert DTOs to HAL representations
  private final CourseDtoAssembler courseDtoAssembler;
  private final UserDtoAssembler userDtoAssembler;
  private final FileModelAssembler fileModelAssembler;

  // Used to add links to paged resources
  private final PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;
  private final PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;
  private final PagedResourcesAssembler<File> filePagedResourcesAssembler;

  private final AuthenticationUtils authenticationUtils;

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
   * Search courses by title, faculty and language.
   *
   * @param title (optional) title of the course to search for
   *              (if not provided, all courses are searched)
   * @param facultyId (optional) ID of the faculty to search for
   *                  (if not provided, all faculties are searched)
   *                  (if provided, but not found, returns status 404)
   * @param language (optional) language of the course to search for
   *                 (if not provided, all languages are searched)
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link CourseDto} objects and page
   *     metadata. If there are no courses, an empty page is returned (without _embedded.courses
   *     field).
   */
  @GetMapping("/search")
  public ResponseEntity<RepresentationModel<CourseDto>> searchCourses(
      @RequestParam(value = "title", required = false) String title,
      @RequestParam(value = "faculty", required = false) Long facultyId,
      @RequestParam(value = "language", required = false) Language language,
      @ModelAttribute PageableRequest request) {
    Page<CourseDto> courses =
        courseService.search(title, facultyId, language, request.toPageable());
    courses = courses.map(courseDtoAssembler::toModel);

    Link selfLink =
        linkTo(methodOn(this.getClass()).searchCourses(null, null, null, null)).withSelfRel();
    Link degreesLink = linkTo(methodOn(this.getClass()).getCourses(null)).withRel("courses");
    Link getByIdLink = linkTo(methodOn(this.getClass()).getCourseById(null)).withRel("course");

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(coursePagedResourcesAssembler.toModel(courses))
            .links(List.of(selfLink, degreesLink, getByIdLink))
            .build());
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
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   * @throws AccessDeniedException if the user is not authorized to add a student to the course,
   *     returns status 403.
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

  /**
   * Remove a course.
   *
   * @param id the ID of the course to remove.
   * @return status 200 (OK) and in body a {@link MessageModel} with a message about the operation.
   * @throws EntityNotFoundException if the course does not exist, returns status 404.
   * @throws AccessDeniedException if the user is not authorized to delete the course, returns
   *     status 403.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   * @throws AccessDeniedException if the user is not authorized to delete the course, returns
   *     status 403.
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or @securityService.isCourseTeacher(#id)")
  public ResponseEntity<MessageModel> deleteCourseById(@PathVariable Long id) {
    courseService.deleteCourse(id);

    MessageModel message = new MessageModel(APIResponses.objectDeleted("Course", id));
    message.add(WebMvcLinkBuilder.linkTo(ApiController.class).withRel("api"));
    message.add(WebMvcLinkBuilder.linkTo(CourseController.class).withRel("courses"));

    return ResponseEntity.ok(message);
  }

  /**
   * Get all files of course with provided ID.
   *
   * @param id the ID of the course to retrieve files from.
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link File} objects and page metadata.
   *     If there are no files, an empty page is returned (without _embedded.files field).
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}/files")
  public ResponseEntity<RepresentationModel<File>> getCourseFiles(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<File> files = courseService.getCourseFiles(id, request.toPageable());
    files = files.map(fileModelAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(filePagedResourcesAssembler.toModel(files)).build());
  }

  /**
   * Upload a file to a course.
   *
   * @param id the ID of the course to upload the file to.
   * @param file the file to upload.
   * @return status 200 (OK) and in body a {@link MessageModel} with a message about the operation
   *     and links to the course and the file.
   * @throws EntityNotFoundException if the course does not exist, returns status 404.
   * @throws FileUploadFailedException if the file could not be uploaded, returns status 500.
   * @throws AccessDeniedException if the user is not authorized to upload files to the course,
   *     returns status 403.
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @PostMapping("/{id}/files")
  @PreAuthorize("hasRole('ADMIN') or @securityService.isCourseTeacher(#id)")
  public ResponseEntity<MessageModel> uploadFiles(
      @PathVariable Long id, @RequestParam("file") MultipartFile file) {
    try {
      courseService.storeFile(file, id);

      // Create response
      MessageModel message =
          new MessageModel(APIResponses.fileUploaded(file.getOriginalFilename()));
      message.add(
          WebMvcLinkBuilder.linkTo(methodOn(CourseController.class).getCourseById(id))
              .withRel("course"));

      return ResponseEntity.status(HttpStatus.OK).body(message);
    } catch (EntityNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new FileUploadFailedException(
          ErrorMessages.fileUploadFailed(file.getOriginalFilename()));
    }
  }

  @GetMapping("/{id}/description")
  public ResponseEntity<String> getCourseDescription(@PathVariable Long id) {
    return ResponseEntity.ok(courseService.getCourseDescription(id));
  }

  /**
   * Check if currently logged in student is enrolled in course with provided ID.
   *
   * @param id the ID of the course to check.
   * @return status 200 (OK) and in body a boolean indicating whether the student is enrolled in the
   *     course.
   */
  @GetMapping("/{id}/is-enrolled")
  public ResponseEntity<Boolean> isUserEnrolled(@PathVariable Long id) {
    CustomUserDetails user = authenticationUtils.getAuthenticatedUser();
    if (user == null) return ResponseEntity.ok(false);

    return ResponseEntity.ok(courseService.isUserEnrolled(id, user.getId()));
  }
}
