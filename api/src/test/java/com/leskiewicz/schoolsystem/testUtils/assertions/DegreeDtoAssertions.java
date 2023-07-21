package com.leskiewicz.schoolsystem.testUtils.assertions;

import com.leskiewicz.schoolsystem.testModels.DegreeDto;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class DegreeDtoAssertions implements DtoAssertion<DegreeDto> {

    @Override
    public void assertDtoInCollection(ResultActions result, int index, DegreeDto dto) throws Exception {
        result
                .andExpect(
                        jsonPath(String.format("$._embedded.degrees[%d].id", index)).value(dto.getId()))
                .andExpect(
                        jsonPath(String.format("$._embedded.degrees[%d].faculty", index)).value(dto.getFaculty()))
                .andExpect(
                        jsonPath(String.format("$._embedded.degrees[%d].fieldOfStudy", index)).value(dto.getFaculty()))
                .andExpect(
                        jsonPath(String.format("$._embedded.degrees[%d].title", index)).value(dto.getTitle()))
                .andExpect(
                        jsonPath(String.format("$._embedded.degrees[%d]._links.self.href", index))
                                .value(String.format("http://localhost/api/degrees/%d", dto.getId())));
    }

    @Override
    public void assertDto(ResultActions result, DegreeDto dto) throws Exception {

    }
}
