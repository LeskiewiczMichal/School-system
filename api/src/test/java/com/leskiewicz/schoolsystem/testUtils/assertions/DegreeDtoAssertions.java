package com.leskiewicz.schoolsystem.testUtils.assertions;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.testModels.DegreeDto;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.web.servlet.ResultActions;

public class DegreeDtoAssertions implements DtoAssertion<DegreeDto> {

  @Override
  public void assertDtoInCollection(ResultActions result, int index, DegreeDto dto)
      throws Exception {
    result
        .andExpect(jsonPath(String.format("$._embedded.degrees[%d].id", index)).value(dto.getId()))
        .andExpect(
            jsonPath(String.format("$._embedded.degrees[%d].faculty", index))
                .value(dto.getFaculty()))
        .andExpect(
            jsonPath(String.format("$._embedded.degrees[%d].fieldOfStudy", index))
                .value(dto.getFieldOfStudy()))
        .andExpect(
            jsonPath(String.format("$._embedded.degrees[%d].title", index)).value(dto.getTitle().toString()))
        .andExpect(
            jsonPath(String.format("$._embedded.degrees[%d]._links.self.href", index))
                .value(String.format("http://localhost/api/degrees/%d", dto.getId())));
  }

  @Override
  public void assertDto(ResultActions result, DegreeDto dto) throws Exception {
    result
        .andExpect(jsonPath("$.id").value(dto.getId()))
        .andExpect(jsonPath("$.title").value(dto.getTitle().toString()))
        .andExpect(jsonPath("$.fieldOfStudy").value(dto.getFieldOfStudy()))
        .andExpect(jsonPath("$.faculty").value(dto.getFaculty()))
        .andExpect(
            jsonPath("$._links.self.href")
                .value(
                    WebMvcLinkBuilder.linkTo(
                            methodOn(DegreeController.class).getDegreeById(dto.getId()))
                        .toString()));

    //                .andExpect(jsonPath("$._links.teachers.href").exists());
  }

  @Override
  public void assertDtoWithAnyId(ResultActions result, DegreeDto dto) throws Exception {
    result
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value(dto.getTitle().toString()))
            .andExpect(jsonPath("$.fieldOfStudy").value(dto.getFieldOfStudy()))
            .andExpect(jsonPath("$.faculty").value(dto.getFaculty()))
            .andExpect(
                    jsonPath("$._links.self.href")
                            .exists());

  }
}
