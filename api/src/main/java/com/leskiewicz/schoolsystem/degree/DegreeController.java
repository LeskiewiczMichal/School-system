package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.degree.utils.DegreeModelAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/degrees")
@AllArgsConstructor
public class DegreeController {

  private final DegreeService degreeService;
  private final DegreeDtoAssembler degreeDtoAssembler;
  private final PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;

  @GetMapping("/{id}")
  public ResponseEntity<DegreeDto> getDegreeById(@PathVariable Long id) {
    DegreeDto degree = degreeService.getById(id);
    degree = degreeDtoAssembler.toModel(degree);

    return ResponseEntity.ok(degree);
  }

  @GetMapping
  public ResponseEntity<RepresentationModel<DegreeDto>> getDegrees(
      @ModelAttribute PageableRequest request) {
    Page<DegreeDto> degrees = degreeService.getDegrees(request.toPageable());
    degrees = degrees.map(degreeDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(degreePagedResourcesAssembler.toModel(degrees)).build());
  }

  @PostMapping
  public ResponseEntity<DegreeDto> createDegree(@Valid @RequestBody CreateDegreeRequest request) {
    DegreeDto degree = degreeService.createDegree(request);
    degree = degreeDtoAssembler.toModel(degree);

    return ResponseEntity.created(degree.getLink("self").get().toUri()).body(degree);
  }
}
