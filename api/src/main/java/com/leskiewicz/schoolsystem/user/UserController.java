package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.service.links.PageableLinksService;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserModelAssembler;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private UserModelAssembler userModelAssembler;
    private PageableLinksService pageableLinksService;

    @GetMapping
    public ResponseEntity<CollectionModel<UserDto>> getUsers(
            @ModelAttribute PageableRequest request) {
        Page<User> users = userService.getUsers(request.toPageable());
        CollectionModel<UserDto> userDtos = userModelAssembler.toCollectionModel(users);
        pageableLinksService.addLinks(userDtos, users, UserController.class, request);

        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserDto userDto = userModelAssembler.toModel(user);

        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patchUser(@RequestBody PatchUserRequest request, @PathVariable Long id) {
        User user = userService.updateUser(request, id);
        UserDto userDto = userModelAssembler.toModel(user);

        return ResponseEntity.ok(userDto);
    }


}
