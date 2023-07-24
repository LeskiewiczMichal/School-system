package com.leskiewicz.schoolsystem.testUtils.assertions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import org.springframework.test.web.servlet.ResultActions;

public class CourseDtoAssertions implements DtoAssertion<CourseDto> {
  @Override
  public void assertDtoInCollection(ResultActions result, int index, CourseDto dto)
      throws Exception {
    result
        .andExpect(jsonPath(String.format("$._embedded.courses[%d].id", index)).value(dto.getId()))
        .andExpect(
            jsonPath(String.format("$._embedded.courses[%d].title", index)).value(dto.getTitle()))
        .andExpect(
            jsonPath(String.format("$._embedded.courses[%d].teacher", index))
                .value(dto.getTeacher()))
        .andExpect(
            jsonPath(String.format("$._embedded.courses[%d].faculty", index))
                .value(dto.getFaculty()))
        .andExpect(
            jsonPath(String.format("$._embedded.courses[%d]._links.self.href", index))
                .value(String.format("http://localhost/api/courses/%d", dto.getId())))
        .andExpect(
            jsonPath(String.format("$._embedded.courses[%d]._links.teacher.href", index)).exists())
        .andExpect(
            jsonPath(String.format("$._embedded.courses[%d]._links.faculty.href", index)).exists());
    // TODO: add students link
    //
    // .andExpect(jsonPath(String.format("$._embedded.courses[%d]._links.students.href", index))
    //                        .exists());
  }

  @Override
  public void assertDto(ResultActions result, CourseDto dto) throws Exception {
    result
        .andExpect(jsonPath("$.id").value(dto.getId()))
        .andExpect(jsonPath("$.title").value(dto.getTitle()))
        .andExpect(jsonPath("$.teacher").value(dto.getTeacher()))
        .andExpect(jsonPath("$.faculty").value(dto.getFaculty()))
        .andExpect(
            jsonPath("$._links.self.href")
                .value(String.format("http://localhost/api/courses/%d", dto.getId())))
        .andExpect(jsonPath("$._links.teacher.href").exists())
        .andExpect(jsonPath("$._links.faculty.href").exists());
    // TODO : add students link
  }

  @Override
  public void assertDtoWithAnyId(ResultActions result, CourseDto dto) throws Exception {
    result
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").value(dto.getTitle()))
        .andExpect(jsonPath("$.teacher").value(dto.getTeacher()))
        .andExpect(jsonPath("$.faculty").value(dto.getFaculty()))
        .andExpect(jsonPath("$._links.self.href").exists())
        .andExpect(jsonPath("$._links.teacher.href").exists())
        .andExpect(jsonPath("$._links.faculty.href").exists());
    // TODO : add students link

  }
}
