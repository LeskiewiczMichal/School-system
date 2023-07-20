package com.leskiewicz.schoolsystem.assembler;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
public class UserModelAssemblerTest {


  @InjectMocks
  private UserModelAssembler userModelAssembler;

  // Variables
  User user;
  Faculty faculty;

  @BeforeEach
  public void setup() {
    user = Mockito.mock(User.class);
    faculty = Mockito.mock(Faculty.class);

    given(faculty.getId()).willReturn(1L);
    given(user.getFaculty()).willReturn(faculty);

    given(UserMapper.convertToDto(any(User.class))).willReturn(
        UserDto.builder().id(1L).firstName("John").lastName("Doe").email("johndoe@example.com")
            .faculty("Informatics").degree("Computer Science").build());
  }

  @Test
  public void testToModel() {
    Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(0L))
        .withSelfRel();
    Link facultyLink = WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyById(
        faculty.getId())).withRel("faculty");

    UserDto userDto = userModelAssembler.toModel(user);

    Assertions.assertEquals(userDto.getLink("self").get(), selfLink);
    Assertions.assertEquals(userDto.getLink("faculty").get(), facultyLink);
  }

  @Test
  public void testToCollectionModel() {
    // Create collection with single user
    Iterable<User> users = Collections.singleton(user);

    CollectionModel<UserDto> userDtos = userModelAssembler.toCollectionModel(users);

    Assert.notEmpty(userDtos.getContent());
    userDtos.getContent().forEach(dto -> {
      // Assert every user has a self link
      Link selfLink = dto.getLink("self").get();
      Link facultyLink = dto.getLink("self").get();

      Assertions.assertNotNull(selfLink, "UserDto should have a self link");
      Assertions.assertNotNull(facultyLink, "UserDto should have a faculty link");
    });
  }
}
