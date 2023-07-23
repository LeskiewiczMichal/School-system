package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing faculties.
 *
 * <p>All endpoints return responses formatted as HAL representations with _links. Collections are
 * return inside _embedded field.
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  // Used to convert DTOs to HAL representations
  private final UserDtoAssembler userDtoAssembler;

  // Used to add links to paged resources
  private final PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;

  /**
   * Get all users.
   *
   * @param request the pageable request containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of UserDto objects and page metadata. If
   *     there are no users, an empty page is returned (without _embedded.users field).
   */
  @GetMapping
  public ResponseEntity<RepresentationModel<UserDto>> getUsers(
      @ModelAttribute PageableRequest request) {
    Page<UserDto> users = userService.getUsers(request.toPageable());
    users = users.map(userDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users)).build());
  }

  /**
   * Get a user by its ID.
   *
   * @param id the ID of the user to retrieve.
   * @return status 200 and the UserDto representing the user with the given ID in the body.
   * @throws EntityNotFoundException if the user does not exist, returns status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   */
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

//  @GetMapping("/{id}/faculty")
//  public ResponseEntity<FacultyDto> getUserFaculty(@PathVariable Long id) {
//    Faculty faculty = userService.getUserFaculty(id);
//    FacultyDto facultyDto = facultyModelAssembler.toModel(faculty);
//
//    return ResponseEntity.ok(facultyDto);
//  }

}
