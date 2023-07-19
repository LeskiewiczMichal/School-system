package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.service.links.PageableLinksService;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserModelAssembler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserModelAssembler userModelAssembler;
  private final PageableLinksService pageableLinksService;
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  @GetMapping
  public ResponseEntity<CollectionModel<UserDto>> getUsers(
      @ModelAttribute PageableRequest request) {
    logger.info("Received request to get users with page number: {} and page size: {}",
        request.getPage(), request.getSize());
    Page<User> users = userService.getUsers(request.toPageable());
    CollectionModel<UserDto> userDtos = userModelAssembler.toCollectionModel(users);
    pageableLinksService.addLinks(userDtos, users, UserController.class, request);
    logger.info("Successfully retrieved {} users", users.getNumberOfElements());

    return ResponseEntity.ok(userDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
//    logger.info("Received request to get user with ID: {}", id);
    User user = userService.getById(id);
    UserDto userDto = userModelAssembler.toModel(user);
//    logger.info("Successfully retrieved user with ID: {}", id);

    return ResponseEntity.ok(userDto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserDto> patchUser(@RequestBody PatchUserRequest request,
      @PathVariable Long id) {
    logger.info("Received request to update user with ID: {}", id);
    User user = userService.updateUser(request, id);
    UserDto userDto = userModelAssembler.toModel(user);
    logger.info("Successfully updated user with ID: {}", id);

    return ResponseEntity.ok(userDto);
  }


}
