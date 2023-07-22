package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import com.leskiewicz.schoolsystem.user.utils.UserModelAssembler;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserModelAssembler userModelAssembler;
  private final FacultyModelAssembler facultyModelAssembler;
  private final UserDtoAssembler userDtoAssembler;
  private final PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;

  @GetMapping
  public ResponseEntity<RepresentationModel<UserDto>> getUsers(
      @ModelAttribute PageableRequest request) {
    Page<UserDto> users = userService.getUsers(request.toPageable());
    users = users.map(userDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users)).build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    UserDto user = userService.getById(id);
    user = userDtoAssembler.toModel(user);

    return ResponseEntity.ok(user);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserDto> patchUser(
      @RequestBody PatchUserRequest request, @PathVariable Long id) {
    UserDto user = userService.updateUser(request, id);
    user = userDtoAssembler.toModel(user);

    return ResponseEntity.ok(user);
  }

  @GetMapping("/{id}/faculty")
  public ResponseEntity<FacultyDto> getUserFaculty(@PathVariable Long id) {
    Faculty faculty = userService.getUserFaculty(id);
    FacultyDto facultyDto = facultyModelAssembler.toModel(faculty);

    return ResponseEntity.ok(facultyDto);
  }

}
