package com.leskiewicz.schoolsystem.testUtils.assertions;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.leskiewicz.schoolsystem.faculty.FacultyController;
// import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.web.servlet.ResultActions;

public class TestAssertions {

  public static void assertUser(ResultActions matchers, UserDto userDto) throws Exception {
    matchers
        .andExpect(jsonPath(String.format("$.id")).value(userDto.getId()))
        .andExpect(jsonPath(String.format("$.firstName")).value(userDto.getFirstName()))
        .andExpect(jsonPath(String.format("$.lastName")).value(userDto.getLastName()))
        .andExpect(jsonPath(String.format("$.email")).value(userDto.getEmail()))
        .andExpect(jsonPath(String.format("$.faculty")).value(userDto.getFaculty()))
        .andExpect(
            jsonPath(String.format("$._links.self.href"))
                .value(String.format("http://localhost/api/users/%d", userDto.getId())));
    if (userDto.getDegree() != null) {
      matchers.andExpect(jsonPath(String.format("$.degree")).value(userDto.getDegree()));
    }
  }

  // endregion

  public static void assertError(ResultActions matchers, String message, String path, int status)
      throws Exception {
    matchers
        .andExpect(jsonPath(String.format("$.message")).value(message))
        .andExpect(jsonPath(String.format("$.statusCode")).value(status))
        .andExpect(jsonPath(String.format("$.path")).value(path));
  }
}
