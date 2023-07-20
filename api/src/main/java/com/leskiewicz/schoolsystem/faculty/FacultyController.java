package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import com.leskiewicz.schoolsystem.service.links.PageableLinksService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/faculties")
@AllArgsConstructor
public class FacultyController {

  private final FacultyService facultyService;
  private final FacultyModelAssembler facultyModelAssembler;
  private final PageableLinksService pageableLinksService;

  @GetMapping
  public ResponseEntity<CollectionModel<FacultyDto>> getFaculties(
      @ModelAttribute PageableRequest request) {
    Page<Faculty> faculties = facultyService.getFaculties(request.toPageable());
    CollectionModel<FacultyDto> facultyDtos = facultyModelAssembler.toCollectionModel(faculties);
    pageableLinksService.addLinks(facultyDtos, faculties, FacultyController.class, request);

    return ResponseEntity.ok(facultyDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<FacultyDto> getFacultyById(@PathVariable Long id) {
    Faculty faculty = facultyService.getById(id);
    FacultyDto facultyDto = facultyModelAssembler.toModel(faculty);

    return ResponseEntity.ok(facultyDto);
  }
}
