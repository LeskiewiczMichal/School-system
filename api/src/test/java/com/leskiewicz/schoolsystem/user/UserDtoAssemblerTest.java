package com.leskiewicz.schoolsystem.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.user.UserController;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserDtoAssembler;
import com.leskiewicz.schoolsystem.user.utils.UserMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@ExtendWith(MockitoExtension.class)
public class UserDtoAssemblerTest {

  // Variables
  UserDto user;
  @InjectMocks private UserDtoAssembler userDtoAssembler;

  @BeforeEach
  public void setup() {

    user =
        UserDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("johndoe@example.com")
            .faculty("Informatics")
            .degree("Computer Science")
            .facultyId(1L)
            .teacherDetailsId(1L)
            .build();
  }

  @Test
  public void testToModelAddsCorrectLinks() {
    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(1L)).withSelfRel();
    Link facultyLink =
        WebMvcLinkBuilder.linkTo(
                methodOn(FacultyController.class).getFacultyById(user.getFacultyId()))
            .withRel("faculty");
    Link coursesLink =
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserCourses(user.getId(), null))
            .withRel("courses");
    Link teacherDetailsLink =
        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getTeacherDetails(user.getId()))
            .withRel("teacherDetails");

    UserDto userDto = userDtoAssembler.toModel(user);

    Assertions.assertEquals(selfLink, userDto.getLink("self").get());
    Assertions.assertEquals(facultyLink, userDto.getLink("faculty").get());
    Assertions.assertEquals(coursesLink, userDto.getLink("courses").get());
    Assertions.assertEquals(teacherDetailsLink, userDto.getLink("teacherDetails").get());
  }
}
