package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.testModels.DegreeDto;
import com.leskiewicz.schoolsystem.testUtils.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.assertions.DegreeDtoAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class DegreeControllerTest extends GenericControllerTest<DegreeDto> {

  private static final String BASE_URL = "/api/degrees";
  DegreeDtoAssertions degreeDtoAssertions;
  @Autowired private MockMvc mvc;
  // Variables
  private ObjectMapper mapper;
  private RequestUtils requestUtils;

  @BeforeEach
  public void setUp() {
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    requestUtils = new RequestUtilsImpl(mvc, mapper);

    degreeDtoAssertions = new DegreeDtoAssertions();
  }

  @Test
  public void getDegreesTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, BASE_URL, 1);
  }

  static Stream<Arguments> getApiCollectionResponsesProvider() {
   // Variables to check
    DegreeDtoAssertions assertions = new DegreeDtoAssertions();
    DegreeDto computerScience = DegreeDto.builder().id(101L).title(DegreeTitle.BACHELOR_OF_SCIENCE).fieldOfStudy("Computer Science").faculty("Informatics").build();
    DegreeDto softwareEngineering = DegreeDto.builder().id(102L).title(DegreeTitle.MASTER).fieldOfStudy("Software Engineering").faculty("Informatics").build();
    DegreeDto nano = DegreeDto.builder().id(103L).title(DegreeTitle.BACHELOR).fieldOfStudy("Nano").faculty("Biology").build();
    DegreeDto doctorNano = DegreeDto.builder().id(111L).title(DegreeTitle.DOCTOR).fieldOfStudy("Nano").faculty("Mathematics").build();

    DegreeDto physics = DegreeDto.builder().id(119L).title(DegreeTitle.BACHELOR).fieldOfStudy("Physics").faculty("Sociology").build();
    DegreeDto math = DegreeDto.builder().id(120L).title(DegreeTitle.DOCTOR).fieldOfStudy("Math").faculty("Law").build();

    // Arguments
    Arguments noParams = Arguments.of(
            BASE_URL, Arrays.asList(computerScience, softwareEngineering, nano), assertions);
    Arguments pageOne = Arguments.of(
            BASE_URL + "?page=1", Arrays.asList(doctorNano), assertions);
    Arguments descending = Arguments.of(
            BASE_URL + "?direction=desc", Arrays.asList(math, physics), assertions);
    Arguments sortByIdDescending = Arguments.of(
            BASE_URL + "?sort=id&direction=desc", Arrays.asList(math, physics), assertions);
    Arguments pageSize20 = Arguments.of(
            BASE_URL + "?size=20", Arrays.asList(computerScience, softwareEngineering,nano), assertions);

    return Stream.of(noParams, pageOne, descending, sortByIdDescending, pageSize20);

  }
}
