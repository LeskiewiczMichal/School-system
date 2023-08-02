package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyDtoAssembler;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * REST controller for managing {@link Faculty}.
 *
 * <p>All endpoints return responses formatted as HAL representations with _links. Collections are
 * return inside _embedded field.
 */
@RestController
@RequestMapping("/api/faculties")
@AllArgsConstructor
public class FacultyController {

  private final FacultyService facultyService;

  // Used to convert DTOs to HAL representations
  private final FacultyDtoAssembler facultyDtoAssembler;
  private final DegreeDtoAssembler degreeDtoAssembler;
  private final UserDtoAssembler userDtoAssembler;
  private final CourseDtoAssembler courseDtoAssembler;

  // Used to add links to paged resources
  private final PagedResourcesAssembler<FacultyDto> facultyPagedResourcesAssembler;
  private final PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  private final PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;
  private final PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  /**
   * Get all faculties.
   *
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link FacultyDto} objects and page
   *     metadata. If there are no faculties, an empty page is returned (without _embedded.faculties
   *     field).
   */
  @GetMapping
  public ResponseEntity<RepresentationModel<FacultyDto>> getFaculties(
      @ModelAttribute PageableRequest request) {
    Page<FacultyDto> faculties = facultyService.getFaculties(request.toPageable());
    faculties = faculties.map(facultyDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(facultyPagedResourcesAssembler.toModel(faculties)).build());
  }

  /**
   * Get a faculty by its ID.
   *
   * @param id the ID of the faculty to retrieve.
   * @return status 200 and the {@link FacultyDto} representing the faculty with the given ID in the
   *     body.
   * @throws EntityNotFoundException if the faculty does not exist, returns status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}")
  public ResponseEntity<FacultyDto> getFacultyById(@PathVariable Long id) {
    FacultyDto faculty = facultyService.getById(id);
    faculty = facultyDtoAssembler.toModel(faculty);

    return ResponseEntity.ok(faculty);
  }

  /**
   * Creates a new faculty based on the given request.
   *
   * @param request The {@link CreateFacultyRequest} containing the data to create the faculty.
   * @return status 201 with created {@link FacultyDto} in the body.
   * @throws EntityAlreadyExistsException and returns status 400 if faculty with the same name
   *     already exists.
   * @throws MethodArgumentNotValidException, returns status 400 and body with path, message about
   *     missing fields, statusCode if the request is invalid.
   * @throws AccessDeniedException and returns status 403 if the user is not an admin.
   */
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<FacultyDto> createFaculty(
      @Valid @RequestBody CreateFacultyRequest request) {
    FacultyDto faculty = facultyService.createFaculty(request);
    faculty = facultyDtoAssembler.toModel(faculty);

    return ResponseEntity.created(faculty.getLink("self").get().toUri()).body(faculty);
  }

  /**
   * Updates a faculty based on the given request.
   *
   * @param request The {@link PatchFacultyRequest} containing the data to update in the faculty.
   * @param id The ID of the faculty to update.
   * @return status 200 with modified {@link FacultyDto} in the body.
   * @throws EntityNotFoundException and returns status 404 if the faculty does not exist.
   * @throws EntityAlreadyExistsException and returns status 400 if faculty with the same name as
   *     provided to the request already exists.
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   * @throws AccessDeniedException and returns status 403 if the user is not an admin.
   */
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<FacultyDto> updateFaculty(
      @RequestBody PatchFacultyRequest request, @PathVariable Long id) {
    FacultyDto faculty = facultyService.updateFaculty(request, id);
    faculty = facultyDtoAssembler.toModel(faculty);

    return ResponseEntity.ok(faculty);
  }

  /**
   * Get all students of faculty with provided ID.
   *
   * @param id the ID of the faculty to retrieve students from.
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link UserDto} objects and page
   *     metadata. If there are no students, an empty page is returned (without _embedded.users
   *     field).
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}/students")
  public ResponseEntity<RepresentationModel<UserDto>> getFacultyStudents(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<UserDto> users =
        facultyService.getFacultyUsers(id, request.toPageable(), Role.ROLE_STUDENT);
    users = users.map(userDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users)).build());
  }

  /**
   * Get all teachers of faculty with provided ID.
   *
   * @param id the ID of the faculty to retrieve teachers from.
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link UserDto} objects and page
   *     metadata. If there are no teachers, an empty page is returned (without _embedded.users
   *     field).
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}/teachers")
  public ResponseEntity<RepresentationModel<UserDto>> getFacultyTeachers(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<UserDto> users =
        facultyService.getFacultyUsers(id, request.toPageable(), Role.ROLE_TEACHER);
    users = users.map(userDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users)).build());
  }

  /**
   * Get all degrees of faculty with provided ID.
   *
   * @param id the ID of the faculty to retrieve degrees from.
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link DegreeDto} objects and page
   *     metadata. If there are no degrees, an empty page is returned (without _embedded.degrees
   *     field).
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}/degrees")
  public ResponseEntity<RepresentationModel<DegreeDto>> getFacultyDegrees(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<DegreeDto> degrees = facultyService.getFacultyDegrees(id, request.toPageable());
    degrees = degrees.map(degreeDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(degreePagedResourcesAssembler.toModel(degrees)).build());
  }

  /**
   * Get all courses of faculty with provided ID.
   *
   * @param id the ID of the faculty to retrieve courses from.
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link CourseDto} objects and page
   *     metadata. If there are no courses, an empty page is returned (without _embedded.courses
   *     field).
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}/courses")
  public ResponseEntity<RepresentationModel<CourseDto>> getFacultyCourses(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<CourseDto> courses = facultyService.getFacultyCourses(id, request.toPageable());
    courses = courses.map(courseDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(coursePagedResourcesAssembler.toModel(courses)).build());
  }
}
