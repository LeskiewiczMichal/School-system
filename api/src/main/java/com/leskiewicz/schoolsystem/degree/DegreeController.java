package com.leskiewicz.schoolsystem.degree;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/degrees")
@AllArgsConstructor
public class DegreeController {

  private final DegreeService degreeService;

  @GetMapping
  public fi
}
