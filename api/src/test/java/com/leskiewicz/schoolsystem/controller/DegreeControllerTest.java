package com.leskiewicz.schoolsystem.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.generic.GenericControllerTest;
import com.leskiewicz.schoolsystem.testModels.DegreeDto;
import com.leskiewicz.schoolsystem.testUtils.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.assertions.TestAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.DegreeDtoAssertions;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class DegreeControllerTest extends GenericControllerTest<DegreeDto> {

  private static final String BASE_URL = "/api/degrees";
  private static final String SINGLE_URL = BASE_URL + "/";
  DegreeDtoAssertions degreeDtoAssertions;
  @Autowired private MockMvc mvc;
  // Variables
  private ObjectMapper mapper;
  private RequestUtils requestUtils;

  //region Providers

  static Stream<Arguments> createDegreeReturns400OnWrongRequestProvider() {
    Arguments nullTitle =
        Arguments.of(new CreateDegreeRequest(null, "Test", "Law"), "Degree title required");

    Arguments nullFieldOfStudy =
        Arguments.of(
            new CreateDegreeRequest(DegreeTitle.BACHELOR, null, "Law"),
            "Degree field of study required");

    Arguments nullFaculty =
        Arguments.of(
            new CreateDegreeRequest(DegreeTitle.BACHELOR, "Test", null), "Faculty name required");

    Arguments nullRequest =
        Arguments.of(
            new CreateDegreeRequest(null, null, null),
            "Faculty name required; Degree field of study required; Degree title required");

    return Stream.of(nullTitle, nullFieldOfStudy, nullFaculty, nullRequest);
  }
  //endregion

  @BeforeEach
  public void setUp() {
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    requestUtils = new RequestUtilsImpl(mvc, mapper);

    degreeDtoAssertions = new DegreeDtoAssertions();
  }

  //region CreateDegree tests
  @Test
  public void getDegreesTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, BASE_URL, 1);
  }

  @Test
  public void createDegreeReturnsCorrectDegreeAndLocationHeader() throws Exception {
    CreateDegreeRequest request = new CreateDegreeRequest(DegreeTitle.BACHELOR, "Test", "Law");
    ResultActions result = requestUtils.performPostRequest(BASE_URL, request, status().isCreated());

    DegreeDto expected =
        DegreeDto.builder()
            .id(1L)
            .title(DegreeTitle.BACHELOR)
            .fieldOfStudy("Test")
            .faculty("Law")
            .build();

    // Assert degreeDto in response body
    degreeDtoAssertions.assertDto(result, expected);

    // Assert correct location header
    String expectedLocation = "http://localhost/api/degrees/1";
    result.andExpect(MockMvcResultMatchers.header().string("Location", expectedLocation));
  }

  @ParameterizedTest
  @MethodSource("createDegreeReturns400OnWrongRequestProvider")
  public void createDegreeReturns400OnWrongRequest(
      CreateDegreeRequest request, String expectedMessage) throws Exception {

    ResultActions result =
        requestUtils.performPostRequest(BASE_URL, request, status().isBadRequest());

    TestAssertions.assertError(result, expectedMessage, BASE_URL, 400);
  }

  @Test
  public void createDegreeReturns400OnDegreeWithAlreadyExists() throws Exception {
    CreateDegreeRequest request =
        new CreateDegreeRequest(DegreeTitle.BACHELOR_OF_SCIENCE, "Computer Science", "Informatics");
    ResultActions result =
        requestUtils.performPostRequest(BASE_URL, request, status().isBadRequest());

    TestAssertions.assertError(
        result,
        "Degree with title: BACHELOR_OF_SCIENCE in Computer Science on faculty: Informatics already exists",
        BASE_URL,
        400);
  }
  //endregion
}
