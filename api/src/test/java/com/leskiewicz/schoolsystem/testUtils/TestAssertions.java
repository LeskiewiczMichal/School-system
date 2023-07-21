package com.leskiewicz.schoolsystem.testUtils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.leskiewicz.schoolsystem.faculty.FacultyController;
// import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.web.servlet.ResultActions;

public class TestAssertions {

  // region User
  public static void assertUserInCollection(
      ResultActions matchers,
      int index,
      long id,
      String firstName,
      String lastName,
      String email,
      String faculty,
      String degree)
      throws Exception {
    matchers
        .andExpect(jsonPath(String.format("$._embedded.users[%d].id", index)).value(id))
        .andExpect(
            jsonPath(String.format("$._embedded.users[%d].firstName", index)).value(firstName))
        .andExpect(jsonPath(String.format("$._embedded.users[%d].lastName", index)).value(lastName))
        .andExpect(jsonPath(String.format("$._embedded.users[%d].email", index)).value(email))
        .andExpect(jsonPath(String.format("$._embedded.users[%d].faculty", index)).value(faculty))
        .andExpect(
            jsonPath(String.format("$._embedded.users[%d]._links.self.href", index))
                .value(String.format("http://localhost/api/users/%d", id)));
    if (degree != null) {
      matchers.andExpect(
          jsonPath(String.format("$._embedded.users[%d].degree", index)).value(degree));
    }
  }

  public static void assertUser(
      ResultActions matchers,
      long id,
      String firstName,
      String lastName,
      String email,
      String faculty,
      String degree)
      throws Exception {
    matchers
        .andExpect(jsonPath(String.format("$.id")).value(id))
        .andExpect(jsonPath(String.format("$.firstName")).value(firstName))
        .andExpect(jsonPath(String.format("$.lastName")).value(lastName))
        .andExpect(jsonPath(String.format("$.email")).value(email))
        .andExpect(jsonPath(String.format("$.faculty")).value(faculty))
        .andExpect(
            jsonPath(String.format("$._links.self.href"))
                .value(String.format("http://localhost/api/users/%d", id)));
    if (degree != null) {
      matchers.andExpect(jsonPath(String.format("$.degree")).value(degree));
    }
  }

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

  // region Faculty
  public static void assertFacultyInCollection(
      ResultActions matchers, FacultyDto faculty, Integer index) throws Exception {
    matchers
        .andExpect(
            jsonPath(String.format("$._embedded.faculties[%d].id", index)).value(faculty.getId()))
        .andExpect(
            jsonPath(String.format("$._embedded.faculties[%d].name", index))
                .value(faculty.getName()))
        .andExpect(
            jsonPath(String.format("$._embedded.faculties[%d]._links.self.href", index))
                .value(String.format("http://localhost/api/users/%d", faculty.getId())));
  }

  public static void assertFaculty(ResultActions matchers, FacultyDto faculty) throws Exception {
    matchers
        .andExpect(jsonPath(String.format("$.id")).value(faculty.getId()))
        .andExpect(jsonPath(String.format("$.name")).value(faculty.getName()))
        .andExpect(
            jsonPath(String.format("$._links.self.href"))
                .value(
                    WebMvcLinkBuilder.linkTo(
                            methodOn(FacultyController.class).getFacultyById(faculty.getId()))
                        .toString()));
  }
  //

  public static void assertLink(ResultActions matchers, String rel, String href) throws Exception {
    matchers.andExpect(jsonPath(String.format("$._links.%s.href", rel)).value(href));
  }

  public static void assertError(ResultActions matchers, String message, String path, int status)
      throws Exception {
    matchers
        .andExpect(jsonPath(String.format("$.message")).value(message))
        .andExpect(jsonPath(String.format("$.statusCode")).value(status))
        .andExpect(jsonPath(String.format("$.path")).value(path));
  }
}
