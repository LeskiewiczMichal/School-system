package com.leskiewicz.schoolsystem.controller;

import com.leskiewicz.schoolsystem.assembler.UserModelAssemblerImpl;
import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.dto.response.PageableResponse;
import com.leskiewicz.schoolsystem.mapper.UserMapperImpl;
import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.repository.UserRepository;
import com.leskiewicz.schoolsystem.service.LinksService;
import com.leskiewicz.schoolsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private UserMapperImpl userMapperImpl;
    private LinksService linksService;
    private UserModelAssemblerImpl userModelAssembler;
    private UserRepository userRepository;

//    @GetMapping
//    public ResponseEntity<PageableResponse<EntityModel<UserDto>>> getUsers(@ModelAttribute PageableRequest request) {
//        Page<User> usersPage = userService.getUsers(request.toPageable());
//        Link selfLink = WebMvcLinkBuilder
//                .linkTo(methodOn(UserController.class)
////                .getUsers(0, 10, "id", null))
//                        .getUsers(PageableRequest.builder()
//                                .page(0)
//                                .size(10)
//                                .sort("id")
//                                .direction("ASC")
//                                .build()))
//                .withSelfRel();
//        List<EntityModel<UserDto>> userModels = userService.toUserDtos(usersPage).stream()
//                .map(userDto -> {
////                    List<Link> links = linksService.addLinks(userDto);
//                    return EntityModel.of(userDto, selfLink);
//                })
//                .collect(Collectors.toList());
//
//
//        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(usersPage.getSize(), usersPage.getNumber(),
//                usersPage.getTotalElements(), usersPage.getTotalPages());
//        PagedModel<EntityModel<UserDto>> pagedModel = PagedModel.of(userModels, metadata);
//        // add links to pagedModel
//
//        PageableResponse<EntityModel<UserDto>> response = PageableResponse.<EntityModel<UserDto>>builder()
//                .pagedModel(pagedModel)
//                .currentPage(usersPage.getNumber())
//                .totalPages(usersPage.getTotalPages())
//                .totalElements(usersPage.getTotalElements())
//                .build();
//        response.addLinks(usersPage, request);
//
//        return ResponseEntity.ok(response);
//    }

//    @GetMapping
//    public ResponseEntity<PagedModel<EntityModel<UserDto>>> getUsers(@ModelAttribute PageableRequest request) {
//        Page<User> usersPage = userService.getUsers(request.toPageable());
//        Link selfLink = WebMvcLinkBuilder
//                .linkTo(methodOn(UserController.class)
////                .getUsers(0, 10, "id", null))
//                        .getUsers(PageableRequest.builder()
//                                .page(0)
//                                .size(10)
//                                .sort("id")
//                                .direction("ASC")
//                                .build()))
//                .withSelfRel();
//        List<EntityModel<UserDto>> userModels = userService.toUserDtos(usersPage).stream()
//                .map(userDto -> EntityModel.of(userDto, selfLink))
//                .collect(Collectors.toList());
//
//        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(usersPage.getSize(), usersPage.getNumber(),
//                usersPage.getTotalElements(), usersPage.getTotalPages());
//        PagedModel<EntityModel<UserDto>> response = PagedModel.of(userModels, metadata);
//        // add links to response
//
//        return ResponseEntity.ok(response);
//    }


    @GetMapping
    public ResponseEntity<CollectionModel<UserDto>> getUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsers(pageable);
        CollectionModel<UserDto> userDtos = userModelAssembler.toCollectionModel(users);

        Link selfLink = linkTo(UserController.class).withSelfRel();
        String uriTemplate = selfLink.getHref() + "{?size,page}";
        selfLink = Link.of(uriTemplate).withSelfRel();

        if (users.hasNext()) {
            Link next = linkTo(methodOn(UserController.class).getUsers(users.nextPageable().getPageNumber(), users.getSize())).withRel("next");
            userDtos.add(next);
        }
        if (users.hasPrevious()) {
            Link prev = linkTo(methodOn(UserController.class).getUsers(users.previousPageable().getPageNumber(), users.getSize())).withRel("prev");
            userDtos.add(prev);
        }

        userDtos.add(selfLink);

        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
//        UserDto userDto = userMapperImpl.convertToDto(user);
//        linksService.addLinks(userDto);
        UserDto userDto = userModelAssembler.toModel(user);

        return ResponseEntity.ok(userDto);
    }


}
