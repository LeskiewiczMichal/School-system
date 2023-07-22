package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.service.links.PageableLinksService;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserModelAssembler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculties")
@AllArgsConstructor
public class FacultyController {

  private final FacultyService facultyService;
  private final FacultyModelAssembler facultyModelAssembler;
  private final PageableLinksService pageableLinksService;
  private final UserModelAssembler userModelAssembler;
  PagedResourcesAssembler<FacultyDto> facultyPagedResourcesAssembler;
  PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;

  @GetMapping
  public ResponseEntity<RepresentationModel<FacultyDto>> getFaculties(
      @ModelAttribute PageableRequest request) {
    Page<FacultyDto> faculties = facultyService.getFaculties(request.toPageable());

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(facultyPagedResourcesAssembler.toModel(faculties)).build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<FacultyDto> getFacultyById(@PathVariable Long id) {
    Faculty faculty = facultyService.getById(id);
    FacultyDto facultyDto = facultyModelAssembler.toModel(faculty);

    return ResponseEntity.ok(facultyDto);
  }

  @PostMapping
  public ResponseEntity<FacultyDto> createFaculty(
      @Valid @RequestBody CreateFacultyRequest request) {
    Faculty faculty = facultyService.createFaculty(request);
    FacultyDto facultyDto = facultyModelAssembler.toModel(faculty);

    return ResponseEntity.created(facultyDto.getLink("self").get().toUri()).body(facultyDto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<FacultyDto> updateFaculty(
      @RequestBody PatchFacultyRequest request, @PathVariable Long id) {
    Faculty faculty = facultyService.updateFaculty(request, id);
    FacultyDto facultyDto = facultyModelAssembler.toModel(faculty);

    return ResponseEntity.ok(facultyDto);
  }

  @GetMapping("/{id}/students")
  public ResponseEntity<RepresentationModel<UserDto>> getFacultyStudents(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<UserDto> users =
        facultyService.getFacultyUsers(id, request.toPageable(), Role.ROLE_STUDENT);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users)).build());
  }

  @GetMapping("/{id}/teachers")
  public ResponseEntity<RepresentationModel<UserDto>> getFacultyTeachers(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<UserDto> users =
        facultyService.getFacultyUsers(id, request.toPageable(), Role.ROLE_TEACHER);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users)).build());
  }

  @GetMapping("/{id}/degrees")
  public ResponseEntity<RepresentationModel<DegreeDto>> getFacultyDegrees(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<DegreeDto> degrees = facultyService.getFacultyDegrees(id, request.toPageable());

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(degreePagedResourcesAssembler.toModel(degrees)).build());
  }
}
