package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.authentication.SecurityService;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.MessageModel;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.APIResponses;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.PatchTeacherDetailsRequest;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsModelAssembler;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing {@link User}.
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
  private final CourseDtoAssembler courseDtoAssembler;
  private final TeacherDetailsModelAssembler teacherDetailsModelAssembler;

  // Used to add links to paged resources
  private final PagedResourcesAssembler<UserDto> userPagedResourcesAssembler;
  private final PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  /**
   * Get all users.
   *
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link UserDto} objects and page
   *     metadata. If there are no users, an empty page is returned (without _embedded.users field).
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
   * Search users by first name, last name and role.
   *
   * @param firstName (optional) first name of the user to search for
   *              (if not provided, all users first names are searched)
   * @param lastName (optional) last name of the user to search for
   *                  (if not provided, all users last names are searched)
   * @param role {@link Role} (optional) role of the user to search for
   *                 (if not provided, all roles are searched)
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link UserDto} objects and page
   *     metadata. If there are no users, an empty page is returned (without _embedded.users
   *     field).
   */
  @GetMapping("/search")
  public ResponseEntity<RepresentationModel<UserDto>> searchUsers(
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "lastName", required = false) String lastName,
      @RequestParam(value = "role", required = false) Role role,
      @ModelAttribute PageableRequest request) {
    Page<UserDto> users = userService.search(lastName, firstName, role, request.toPageable());
    users = users.map(userDtoAssembler::toModel);

    Link selfLink =
        linkTo(methodOn(UserController.class).searchUsers(firstName, lastName, role, request))
            .withSelfRel();
    Link usersLink = linkTo(methodOn(UserController.class).getUsers(null)).withRel("users");
    Link getByIdLink = linkTo(methodOn(UserController.class).getUserById(null)).withRel("user");

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(userPagedResourcesAssembler.toModel(users))
            .links(List.of(selfLink, usersLink, getByIdLink))
            .build());
  }

  /**
   * Get a user by its ID.
   *
   * @param id the ID of the user to retrieve.
   * @return status 200 and the {@link UserDto} representing the user with the given ID in the body.
   * @throws EntityNotFoundException if the user does not exist, returns status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    UserDto user = userService.getById(id);
    user = userDtoAssembler.toModel(user);

    return ResponseEntity.ok(user);
  }

  /**
   * Updates a user based on the given request.
   *
   * @param request The {@link PatchUserRequest} containing the data to update in the user.
   * @param id The ID of the user to update.
   * @return status 200 with modified {@link UserDto} in the body.
   * @throws EntityNotFoundException and returns status 404 if the user does not exist.
   * @throws EntityAlreadyExistsException and returns status 400 if user with the same email as
   *     provided already exists.
   * @throws MethodArgumentTypeMismatchException and returns status 400 if the ID is not a number.
   * @throws AccessDeniedException and returns status 403 if the user is not the owner of the
   *     resource.
   */
  @PatchMapping("/{id}")
  @PreAuthorize("@securityServiceImpl.isSelf(#id)")
  public ResponseEntity<UserDto> patchUser(
      @RequestBody PatchUserRequest request, @PathVariable Long id) {
    UserDto user = userService.updateUser(request, id);
    user = userDtoAssembler.toModel(user);

    return ResponseEntity.ok(user);
  }

  /**
   * Get all courses of user with provided ID.
   *
   * @param id the ID of the user to retrieve courses from.
   * @param request the {@link PageableRequest} containing sorting, pagination, etc.
   * @return status 200 (OK) and in body the paged list of {@link CourseDto} objects and page
   *     metadata. If there are no courses, an empty page is returned (without _embedded.courses
   *     field).
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}/courses")
  public ResponseEntity<RepresentationModel<CourseDto>> getUserCourses(
      @PathVariable Long id, @ModelAttribute PageableRequest request) {
    Page<CourseDto> courses = userService.getUserCourses(id, request.toPageable());
    courses = courses.map(courseDtoAssembler::toModel);

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(coursePagedResourcesAssembler.toModel(courses)).build());
  }

  /**
   * Get teacher details of user with provided ID.
   *
   * @param id the ID of the user to retrieve teacher details from.
   * @return status 200 (OK) and in body the {@link TeacherDetails} object.
   * @throws EntityNotFoundException if teacher details for given user id does not exist, returns
   *     status 404.
   * @throws IllegalArgumentException if the ID is a string, returns status 400.
   * @throws MethodArgumentTypeMismatchException if the ID is not a number, returns status 400.
   */
  @GetMapping("/{id}/teacher-details")
  public ResponseEntity<TeacherDetails> getTeacherDetails(@PathVariable Long id) {
    TeacherDetails teacherDetails = userService.getTeacherDetails(id);
    teacherDetails = teacherDetailsModelAssembler.toModel(teacherDetails);

    return ResponseEntity.ok(teacherDetails);
  }

  /**
   * Updates teacher details of user with provided ID.
   *
   * @param request The {@link PatchTeacherDetailsRequest} containing the data to update in the
   *     teacher details.
   * @param id The ID of the user to update teacher details.
   * @return status 200 with modified {@link TeacherDetails} in the body.
   * @throws EntityNotFoundException and returns status 404 if the teacher details does not exist.
   * @throws MethodArgumentTypeMismatchException and returns status 400 if the ID is not a number.
   * @throws AccessDeniedException and returns status 403 if the user is not the owner of the
   *     resource.
   */
  @PatchMapping("/{id}/teacher-details")
  @PreAuthorize("@securityServiceImpl.isSelf(#id)")
  public ResponseEntity<TeacherDetails> patchTeacherDetails(
      @RequestBody PatchTeacherDetailsRequest request, @PathVariable Long id) {
    TeacherDetails updatedTeacherDetails = userService.updateTeacherDetails(request, id);
    updatedTeacherDetails = teacherDetailsModelAssembler.toModel(updatedTeacherDetails);

    return ResponseEntity.ok(updatedTeacherDetails);
  }

  /**
   * Update profile picture of user with provided ID.
   *
   * @param id the ID of the user to update profile picture for.
   * @param file the image file to upload.
   * @return status 200 (OK) and in body the message about successful upload.
   */
  @PostMapping("/{id}/profile-picture")
  @PreAuthorize("@securityServiceImpl.isSelf(#id)")
  public ResponseEntity<MessageModel> updateImage(
          @PathVariable Long id, @RequestParam("file") MultipartFile file) {
    userService.addImage(id, file);

    // Create response
    MessageModel message =
            new MessageModel(APIResponses.fileUploaded(file.getOriginalFilename()));
    message.add(linkTo(methodOn(this.getClass()).getUserById(id)).withRel("user"));

    return ResponseEntity.status(HttpStatus.OK).body(message);
  }
}
