package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeModelAssembler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/degrees")
@AllArgsConstructor
public class DegreeController {

  private final DegreeService degreeService;
  private final DegreeModelAssembler degreeModelAssembler;

  @GetMapping("/{id}")
  public ResponseEntity<DegreeDto> getDegreeById(@PathVariable Long id) {
    Degree degree = degreeService.getById(id);
    DegreeDto degreeDto = degreeModelAssembler.toModel(degree);

    return ResponseEntity.ok(degreeDto);
  }
}
