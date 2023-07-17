package com.leskiewicz.schoolsystem.assembler;

import com.leskiewicz.schoolsystem.controller.UserController;
import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.mapper.UserMapper;
import com.leskiewicz.schoolsystem.model.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserDto> {

    private final UserMapper userMapper;

    public UserModelAssembler(UserMapper userMapper) {
        super(UserController.class, UserDto.class);
        this.userMapper = userMapper;
    }

    @Override
    public UserDto toModel(User entity) {
        UserDto userDto = userMapper.convertToDto(entity);

        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(entity.getId())).withSelfRel();
        userDto.add(selfLink);

        return userDto;
    }

    @Override
    public CollectionModel<UserDto> toCollectionModel(Iterable<? extends User> entities) {
        CollectionModel<UserDto> userDtos = super.toCollectionModel(entities);

        return userDtos;
    }
}
