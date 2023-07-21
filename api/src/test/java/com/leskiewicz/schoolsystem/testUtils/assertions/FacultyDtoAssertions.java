package com.leskiewicz.schoolsystem.testUtils.assertions;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;

import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.web.servlet.ResultActions;

public class FacultyDtoAssertions implements DtoAssertion<FacultyDto> {


  @Override
  public void assertDtoInCollection(ResultActions result, int index, FacultyDto dto)
      throws Exception {
    result.andExpect(jsonPath(String.format("$._embedded.faculties[%d].id", index)).value(dto.getId()))
        .andExpect(
            jsonPath(String.format("$._embedded.faculties[%d].name", index)).value(dto.getName()))
        .andExpect(jsonPath(String.format("$._embedded.faculties[%d]._links.self.href", index)).value(
            String.format("http://localhost/api/faculties/%d", dto.getId())));

  }

  @Override
  public void assertDto(ResultActions result, FacultyDto dto) throws Exception {
    result.andExpect(jsonPath(String.format("$.id")).value(dto.getId()))
        .andExpect(jsonPath(String.format("$.name")).value(dto.getName()))
        .andExpect(
            jsonPath(String.format("$._links.self.href")).value(WebMvcLinkBuilder.linkTo(
                methodOn(FacultyController.class).getFacultyById(dto.getId())).toString()))
            .andExpect(jsonPath("$._links.students.href").exists());
  }
}
