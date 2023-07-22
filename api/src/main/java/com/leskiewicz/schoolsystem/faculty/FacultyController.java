package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyDtoAssembler;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.service.links.PageableLinksService;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
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
  private final FacultyDtoAssembler facultyDtoAssembler;
  private final DegreeDtoAssembler degreeDtoAssembler;
  private final UserDtoAssembler userDtoAssembler;
  private final PagedResourcesAssembler<FacultyDto> facultyPagedResourcesAssembler;
  private final PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  private final PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;

  @GetMapping
  public ResponseEntity<RepresentationModel<FacultyDto>> getFaculties(
      @ModelAttribute PageableRequest request) {
    Page<FacultyDto> faculties = facultyService.getFaculties(request.toPageable());
    faculties = faculties.map(facultyDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(facultyPagedResourcesAssembler.toModel(faculties)).build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<FacultyDto> getFacultyById(@PathVariable Long id) {
    FacultyDto faculty = facultyService.getById(id);
    faculty = facultyDtoAssembler.toModel(faculty);

    return ResponseEntity.ok(faculty);
  }

  @PostMapping
  public ResponseEntity<FacultyDto> createFaculty(
      @Valid @RequestBody CreateFacultyRequest request) {
    FacultyDto faculty = facultyService.createFaculty(request);
    faculty = facultyDtoAssembler.toModel(faculty);

    return ResponseEntity.created(faculty.getLink("self").get().toUri()).body(faculty);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<FacultyDto> updateFaculty(
      @RequestBody PatchFacultyRequest request, @PathVariable Long id) {
    FacultyDto faculty = facultyService.updateFaculty(request, id);
    faculty = facultyDtoAssembler.toModel(faculty);

    return ResponseEntity.ok(faculty);
  }

  @GetMapping("/{id}/students")
  public ResponseEntity<RepresentationModel<UserDto>> getFacultyStudents(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<UserDto> users =
        facultyService.getFacultyUsers(id, request.toPageable(), Role.ROLE_STUDENT);
    users = users.map(userDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users)).build());
  }

  @GetMapping("/{id}/teachers")
  public ResponseEntity<RepresentationModel<UserDto>> getFacultyTeachers(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<UserDto> users =
        facultyService.getFacultyUsers(id, request.toPageable(), Role.ROLE_TEACHER);
    users = users.map(userDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users)).build());
  }

  @GetMapping("/{id}/degrees")
  public ResponseEntity<RepresentationModel<DegreeDto>> getFacultyDegrees(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<DegreeDto> degrees = facultyService.getFacultyDegrees(id, request.toPageable());
    degrees = degrees.map(degreeDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(degreePagedResourcesAssembler.toModel(degrees)).build());
  }
}
