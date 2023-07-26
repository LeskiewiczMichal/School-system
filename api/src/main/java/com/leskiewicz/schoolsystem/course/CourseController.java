package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  // Used to add links to paged resources
  private final PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

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

  @PostMapping
  public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CreateCourseRequest request) {
    CourseDto course = courseService.createCourse(request);
    course = courseDtoAssembler.toModel(course);

    return ResponseEntity.created(course.getLink("self").get().toUri()).body(course);
  }
}
