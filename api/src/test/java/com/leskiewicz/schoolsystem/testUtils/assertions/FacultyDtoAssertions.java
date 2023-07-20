package com.leskiewicz.schoolsystem.testUtils.assertions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.leskiewicz.schoolsystem.testModels.FacultyDto;
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
}
