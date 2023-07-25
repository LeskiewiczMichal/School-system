package com.leskiewicz.schoolsystem.degree.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.generic.GenericControllerTest;
import com.leskiewicz.schoolsystem.testModels.DegreeDto;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.assertions.DegreeDtoAssertions;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class DegreeGenericControllerProviderTest extends GenericControllerTest<DegreeDto> {

  private static final String BASE_URL = "/api/degrees";
  private static final String SINGLE_URL = BASE_URL + "/";

  /// *** GetDegrees  ***///
  static Stream<Arguments> getApiCollectionResponsesProvider() {
    // Variables to check
    DegreeDtoAssertions assertions = new DegreeDtoAssertions();
    DegreeDto computerScience =
        DegreeDto.builder()
            .id(101L)
            .title(DegreeTitle.BACHELOR_OF_SCIENCE)
            .fieldOfStudy("Computer Science")
            .faculty("Informatics")
            .build();
    DegreeDto softwareEngineering =
        DegreeDto.builder()
            .id(102L)
            .title(DegreeTitle.MASTER)
            .fieldOfStudy("Software Engineering")
            .faculty("Informatics")
            .build();
    DegreeDto nano =
        DegreeDto.builder()
            .id(103L)
            .title(DegreeTitle.BACHELOR)
            .fieldOfStudy("Nano")
            .faculty("Biology")
            .build();
    DegreeDto doctorNano =
        DegreeDto.builder()
            .id(111L)
            .title(DegreeTitle.DOCTOR)
            .fieldOfStudy("Nano")
            .faculty("Mathematics")
            .build();

    DegreeDto physics =
        DegreeDto.builder()
            .id(119L)
            .title(DegreeTitle.BACHELOR)
            .fieldOfStudy("Physics")
            .faculty("Sociology")
            .build();
    DegreeDto math =
        DegreeDto.builder()
            .id(120L)
            .title(DegreeTitle.DOCTOR)
            .fieldOfStudy("Math")
            .faculty("Law")
            .build();

    // Arguments
    Arguments noParams =
        Arguments.of(
            BASE_URL, Arrays.asList(computerScience, softwareEngineering, nano), assertions);
    Arguments pageOne = Arguments.of(BASE_URL + "?page=1", Arrays.asList(doctorNano), assertions);
    Arguments descending =
        Arguments.of(BASE_URL + "?sort=id,desc", Arrays.asList(math, physics), assertions);
    Arguments pageSize20 =
        Arguments.of(
            BASE_URL + "?size=20",
            Arrays.asList(computerScience, softwareEngineering, nano),
            assertions);

    return Stream.of(noParams, pageOne, descending, pageSize20);
  }

  /// *** GetDegree  ***///
  static Stream<Arguments> getApiSingleItemErrorsProvider() {
    Arguments status400OnStringProvided =
        Arguments.of(
            SINGLE_URL + "asdf",
            status().isBadRequest(),
            MediaType.APPLICATION_JSON.toString(),
            "Wrong argument types provided",
            400);

    Arguments status404OnDegreeNotFound =
        Arguments.of(
            SINGLE_URL + "999",
            status().isNotFound(),
            MediaType.APPLICATION_JSON.toString(),
            "Degree with ID: 999 not found",
            404);

    return Stream.of(status400OnStringProvided, status404OnDegreeNotFound);
  }

  static Stream<Arguments> getApiSingleItemResponsesProvider() {
    return Stream.of(
        Arguments.of(
            SINGLE_URL + "101",
            status().isOk(),
            "application/hal+json",
            DegreeDto.builder()
                .id(101L)
                .title(DegreeTitle.BACHELOR_OF_SCIENCE)
                .fieldOfStudy("Computer Science")
                .faculty("Informatics")
                .build(),
            new DegreeDtoAssertions()));
  }

  @Test
  public void getDegreesTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, BASE_URL, 1);
  }
}
