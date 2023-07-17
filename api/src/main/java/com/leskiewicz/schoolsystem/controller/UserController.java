package com.leskiewicz.schoolsystem.controller;

import com.leskiewicz.schoolsystem.assembler.UserModelAssemblerImpl;
import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.service.UserService;
import com.leskiewicz.schoolsystem.service.links.PageableLinksService;
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
    private UserModelAssemblerImpl userModelAssembler;
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


}
