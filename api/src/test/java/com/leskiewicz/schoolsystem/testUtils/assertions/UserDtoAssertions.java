package com.leskiewicz.schoolsystem.testUtils.assertions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.web.servlet.ResultActions;

public class UserDtoAssertions implements DtoAssertion<UserDto> {

  @Override
  public void assertDtoInCollection(ResultActions result, int index, UserDto dto) throws Exception {
    result
        .andExpect(jsonPath(String.format("$._embedded.users[%d].id", index)).value(dto.getId()))
        .andExpect(
            jsonPath(String.format("$._embedded.users[%d].firstName", index))
                .value(dto.getFirstName()))
        .andExpect(
            jsonPath(String.format("$._embedded.users[%d].lastName", index))
                .value(dto.getLastName()))
        .andExpect(
            jsonPath(String.format("$._embedded.users[%d].email", index)).value(dto.getEmail()))
        .andExpect(
            jsonPath(String.format("$._embedded.users[%d].faculty", index)).value(dto.getFaculty()))
        .andExpect(
            jsonPath(String.format("$._embedded.users[%d]._links.self.href", index))
                .value(String.format("http://localhost/api/users/%d", dto.getId())))
        .andExpect(jsonPath(String.format("$._embedded.users[%d]._links.faculty.href", index)).exists())
        .andExpect(jsonPath(String.format("$._embedded.users[%d]._links.courses.href", index)).exists());

    // Degree might be empty when user is teacher
    if (dto.getDegree() != null) {
      result.andExpect(
          jsonPath(String.format("$._embedded.users[%d].degree", index)).value(dto.getDegree()));
      result.andExpect(
          jsonPath(String.format("$._embedded.users[%d]._links.degree.href", index)).exists());
    }
  }

  @Override
  public void assertDto(ResultActions result, UserDto dto) throws Exception {
    result
        .andExpect(jsonPath(String.format("$.id")).value(dto.getId()))
        .andExpect(jsonPath(String.format("$.firstName")).value(dto.getFirstName()))
        .andExpect(jsonPath(String.format("$.lastName")).value(dto.getLastName()))
        .andExpect(jsonPath(String.format("$.email")).value(dto.getEmail()))
        .andExpect(jsonPath(String.format("$.faculty")).value(dto.getFaculty()))
        .andExpect(
            jsonPath(String.format("$._links.self.href"))
                .value(String.format("http://localhost/api/users/%d", dto.getId())))
        .andExpect(jsonPath(String.format("$._links.faculty.href")).exists())
        .andExpect(jsonPath(String.format("$._links.courses.href")).exists());

    // Degree might be empty when user is teacher
    if (dto.getDegree() != null) {
      result.andExpect(jsonPath(String.format("$.degree")).value(dto.getDegree()));
      result.andExpect(
          jsonPath(String.format("$._links.degree.href"))
              .value(
                  WebMvcLinkBuilder.linkTo(DegreeController.class)
                      .slash(dto.getDegreeId())
                      .withSelfRel()
                      .getHref()));
    }
  }

  @Override
  public void assertDtoWithAnyId(ResultActions result, UserDto dto) throws Exception {
    result
        .andExpect(jsonPath(String.format("$.id")).exists())
        .andExpect(jsonPath(String.format("$.firstName")).value(dto.getFirstName()))
        .andExpect(jsonPath(String.format("$.lastName")).value(dto.getLastName()))
        .andExpect(jsonPath(String.format("$.email")).value(dto.getEmail()))
        .andExpect(jsonPath(String.format("$.faculty")).value(dto.getFaculty()))
        .andExpect(jsonPath(String.format("$._links.self.href")).exists())
        .andExpect(jsonPath(String.format("$._links.faculty.href")).exists())
        .andExpect(jsonPath(String.format("$._links.courses.href")).exists());

    // Degree might be empty when user is teacher
    if (dto.getDegree() != null) {
      result.andExpect(jsonPath(String.format("$.degree")).value(dto.getDegree()));
      result.andExpect(jsonPath(String.format("$._links.degree.href")).exists());
    }
  }
}
