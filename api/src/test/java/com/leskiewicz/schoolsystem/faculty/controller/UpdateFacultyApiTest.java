package com.leskiewicz.schoolsystem.faculty.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.TestAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateFacultyApiTest {

  private static final String BASE_FACULTIES = "/api/faculties";

  @Autowired private FacultyRepository facultyRepository;
  @Autowired private MockMvc mvc;

  private ObjectMapper mapper;
  private RequestUtils requestUtils;
  private FacultyDtoAssertions facultyDtoAssertions;
  private Faculty faculty;
  
  @BeforeAll
  public void setUp() {
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    requestUtils = new RequestUtilsImpl(mvc, mapper);
    facultyDtoAssertions = new FacultyDtoAssertions();

    // Create faculty to modify
    faculty = Faculty.builder().name("Test").build();
    facultyRepository.save(faculty);
  }

  @Test
  public void updateFacultyReturnsCorrectFaculty() throws Exception {
    PatchFacultyRequest request = new PatchFacultyRequest("New Name");

    // Call endpoint
    ResultActions result =
        requestUtils.performPatchRequest(BASE_FACULTIES + "/1", request, status().isOk());

    // Create expected facultyDto
    FacultyDto expected = FacultyDto.builder().id(1L).name(request.getName()).build();

    // Assert the facultyDto in response body
    facultyDtoAssertions.assertDto(result, expected);
  }

  @Test
  public void updateFacultyReturns404OnFacultyNotFound() throws Exception {
    PatchFacultyRequest request = new PatchFacultyRequest("New Name");

    // Call endpoint
    ResultActions result =
        requestUtils.performPatchRequest(BASE_FACULTIES + "/999", request, status().isNotFound());

    TestAssertions.assertError(
        result, "Faculty with ID: 999 not found", BASE_FACULTIES + "/999", 404);
  }

  @Test
  public void updateFacultyReturns400OnFacultyWithNameAlreadyExists() throws Exception {
    // Create faculty with the name we want to change to
    Faculty faculty = Faculty.builder().name("Biology").build();
    facultyRepository.save(faculty);

    // Create request to change the name to the one that already exists
    PatchFacultyRequest request = new PatchFacultyRequest(faculty.getName());

    // Call endpoint
    ResultActions result =
        requestUtils.performPatchRequest(BASE_FACULTIES + "/1", request, status().isBadRequest());

    TestAssertions.assertError(
        result, "Faculty with name: Biology already exists", BASE_FACULTIES + "/1", 400);
  }
}
