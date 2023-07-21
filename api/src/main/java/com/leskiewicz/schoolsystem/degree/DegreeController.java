package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeModelAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.service.links.PageableLinksService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/degrees")
@AllArgsConstructor
public class DegreeController {

  private final DegreeService degreeService;
  private final DegreeModelAssembler degreeModelAssembler;
  private final PageableLinksService pageableLinksService;

  @GetMapping("/{id}")
  public ResponseEntity<DegreeDto> getDegreeById(@PathVariable Long id) {
    Degree degree = degreeService.getById(id);
    DegreeDto degreeDto = degreeModelAssembler.toModel(degree);

    return ResponseEntity.ok(degreeDto);
  }

  @GetMapping
  public ResponseEntity<CollectionModel<DegreeDto>> getDegrees(
      @ModelAttribute PageableRequest request) {
    Page<Degree> degrees = degreeService.getDegrees(request.toPageable());
    CollectionModel<DegreeDto> degreeDtos = degreeModelAssembler.toCollectionModel(degrees);
    pageableLinksService.addLinks(degreeDtos, degrees, DegreeController.class, request);

    return ResponseEntity.ok(degreeDtos);
  }

  @PostMapping
  public ResponseEntity<DegreeDto> createDegree(@Valid @RequestBody CreateDegreeRequest request) {
    Degree degree = degreeService.createDegree(request);
    DegreeDto degreeDto = degreeModelAssembler.toModel(degree);

    return ResponseEntity.created(degreeDto.getLink("self").get().toUri()).body(degreeDto);
  }
}
