package com.leskiewicz.schoolsystem.user.utils;

import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserController;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Link selfLink = WebMvcLinkBuilder.linkTo(
        methodOn(UserController.class).getUserById(entity.getId())).withSelfRel();
    Link facultyLink = WebMvcLinkBuilder.linkTo(
        methodOn(FacultyController.class).getFacultyById(entity.getFaculty().getId())).withRel("faculty");

    userDto.add(selfLink);
    userDto.add(facultyLink);

    return userDto;
  }

  @Override
  public CollectionModel<UserDto> toCollectionModel(Iterable<? extends User> entities) {

    return super.toCollectionModel(entities);
  }
}
