package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.authentication.Role;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing faculties.
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

  // Used to add links to paged resources
  private final PagedResourcesAssembler<FacultyDto> facultyPagedResourcesAssembler;
  private final PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  private final PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;

  /**
   * Get all faculties.
   *
   * @param request the pageable request containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of FacultyDto objects and page metadata. If
   *     there are no faculties, an empty page is returned (without _embedded.faculties field).
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
   * @return status 200 and the {@link FacultyDto} representing the faculty with the given ID in the body.
   * @throws EntityNotFoundException if the faculty does not exist, returns status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
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
   * @param request The request containing the data to create the faculty.
   * @return status 201 with created FacultyDto in the body.
   * @throws EntityAlreadyExistsException and returns status 400 if faculty with the same name
   *     already exists.
   * @throws MethodArgumentNotValidException, returns status 400 and body with path, message about
   *     missing fields, statusCode if the request is invalid.   */
  @PostMapping
  public ResponseEntity<FacultyDto> createFaculty(
      @Valid @RequestBody CreateFacultyRequest request) {
    FacultyDto faculty = facultyService.createFaculty(request);
    faculty = facultyDtoAssembler.toModel(faculty);

    return ResponseEntity.created(faculty.getLink("self").get().toUri()).body(faculty);
  }

  /**
   * Updates a faculty based on the given request.
   *
   * @param request The request containing the data to update in the faculty.
   * @param id The ID of the faculty to update.
   * @return status 200 with modified FacultyDto in the body.
   * @throws EntityNotFoundException and returns status 404 if the faculty does not exist.
   * @throws EntityAlreadyExistsException and returns status 400 if faculty with the same name as
   *     provided to the request already exists.
   */
  @PatchMapping("/{id}")
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
   * @param request the pageable request containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of UserDto objects and page metadata. If
   *     there are no students, an empty page is returned (without _embedded.users field).
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
   * @param request the pageable request containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of UserDto objects and page metadata. If
   *     there are no teachers, an empty page is returned (without _embedded.users field).
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
   * @param request the pageable request containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of UserDto objects and page metadata. If
   *     there are no degrees, an empty page is returned (without _embedded.degrees field).
   */
  @GetMapping("/{id}/degrees")
  public ResponseEntity<RepresentationModel<DegreeDto>> getFacultyDegrees(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<DegreeDto> degrees = facultyService.getFacultyDegrees(id, request.toPageable());
    degrees = degrees.map(degreeDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(degreePagedResourcesAssembler.toModel(degrees)).build());
  }
}
