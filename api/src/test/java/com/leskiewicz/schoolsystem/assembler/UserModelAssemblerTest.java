package com.leskiewicz.schoolsystem.assembler;

import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserController;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.user.utils.UserModelAssembler;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class UserModelAssemblerTest {

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserModelAssembler userModelAssembler;

  @BeforeEach
  public void setup() {
    given(userMapper.convertToDto(any(User.class))).willReturn(
        UserDto.builder().id(1L).firstName("John").lastName("Doe").email("johndoe@example.com")
            .faculty("Informatics").degree("Computer Science").build());
  }

  @Test
  public void testToModel() {
    User user = User.builder().id(1L).build();

    Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(1L))
        .withSelfRel();
    UserDto userDto = userModelAssembler.toModel(user);

    Assertions.assertEquals(userDto.getLink("self").get(), selfLink);
  }

  @Test
  public void testToCollectionModel() {
    User user = User.builder().id(1L).build();

    // Create collection with single user
    Iterable<User> users = Collections.singleton(user);

    CollectionModel<UserDto> userDtos = userModelAssembler.toCollectionModel(users);

    Assert.notEmpty(userDtos.getContent());
    userDtos.getContent().forEach(dto -> {
      // Assert every user has a self link
      Link selfLink = dto.getLink("self").get();
      Assertions.assertNotNull(selfLink, "UserDto should have a self link");
    });
  }
}
