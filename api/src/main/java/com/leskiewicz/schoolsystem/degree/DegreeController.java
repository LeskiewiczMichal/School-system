package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
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
 * REST controller for managing degrees.
 *
 * <p>All endpoints return responses formatted as HAL representations with _links.
 */
@RestController
@RequestMapping("/api/degrees")
@AllArgsConstructor
public class DegreeController {

  private final DegreeService degreeService;
  private final DegreeDtoAssembler degreeDtoAssembler;
  private final PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;

  /**
   * Get a degree by ID.
   *
   * @param id the ID of the degree to retrieve.
   * @return status 200 and the DegreeDto representing the degree with the given ID in the body.
   * @throws EntityNotFoundException if the degree does not exist, returning status 404.
   * @throws IllegalArgumentException if the ID is a string, returning status 400.
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
   * @param request the pageable request containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of DegreeDto objects, page metadata. If
   *     there are no degrees, an empty page is returned (with no _embedded.degrees field).
   */
  @GetMapping
  public ResponseEntity<RepresentationModel<DegreeDto>> getDegrees(
      @ModelAttribute PageableRequest request) {
    Page<DegreeDto> degrees = degreeService.getDegrees(request.toPageable());
    degrees = degrees.map(degreeDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(degreePagedResourcesAssembler.toModel(degrees)).build());
  }

  /**
   * Creates a new degree based on the given request.
   *
   * @param request The request containing the data to create the degree.
   * @return status 201 with created DegreeDto in the body.
   * @throws EntityAlreadyExistsException and returns status 400 if a degree with the same title,
   *     field of study, and faculty already exists.
   * @throws EntityNotFoundException and returns status 404 if provided faculty does not exist.
   */
  @PostMapping
  public ResponseEntity<DegreeDto> createDegree(@Valid @RequestBody CreateDegreeRequest request) {
    DegreeDto degree = degreeService.createDegree(request);
    degree = degreeDtoAssembler.toModel(degree);

    return ResponseEntity.created(degree.getLink("self").get().toUri()).body(degree);
  }
}
