package com.leskiewicz.schoolsystem.faculty.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leskiewicz.schoolsystem.generic.GenericControllerTest;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class FacultyGenericControllerProviderTest extends GenericControllerTest<FacultyDto> {

  private static final String BASE_FACULTIES = "/api/faculties";

  // *** GetFaculties ***//
  static Stream<Arguments> getApiCollectionResponsesProvider() {
    // Variables needed for tests
    FacultyDtoAssertions assertions = new FacultyDtoAssertions();
    FacultyDto informatics = FacultyDto.builder().id(101L).name("Informatics").build();
    FacultyDto biology = FacultyDto.builder().id(102L).name("Biology").build();
    FacultyDto electronics = FacultyDto.builder().id(103L).name("Electronics").build();
    FacultyDto chemistry = FacultyDto.builder().id(104L).name("Chemistry").build();
    FacultyDto sociology = FacultyDto.builder().id(111L).name("Sociology").build();
    FacultyDto law = FacultyDto.builder().id(112L).name("Law").build();
    FacultyDto economics = FacultyDto.builder().id(113L).name("Economics").build();

    // Arguments
    Arguments noParams =
        Arguments.of(BASE_FACULTIES, Arrays.asList(informatics, biology, electronics), assertions);

    Arguments pageOne =
        Arguments.of(
            BASE_FACULTIES + "?page=1", Arrays.asList(sociology, law, economics), assertions);

    Arguments descending =
        Arguments.of(
            BASE_FACULTIES + "?sort=id,desc", Arrays.asList(economics, law, sociology), assertions);

    Arguments sortByName =
        Arguments.of(BASE_FACULTIES + "?sort=name", Arrays.asList(biology, chemistry), assertions);

    Arguments pageSize20 =
        Arguments.of(
            BASE_FACULTIES + "?size=20",
            Arrays.asList(informatics, biology, electronics, chemistry),
            assertions);

    return Stream.of(noParams, pageOne, descending, sortByName, pageSize20);
  }

  // *** GetFacultyById ***//

  static Stream<Arguments> getApiSingleItemResponsesProvider() {
    return Stream.of(
        Arguments.of(
            "/api/faculties/101",
            status().isOk(),
            "application/hal+json",
            FacultyDto.builder().id(101L).name("Informatics").build(),
            new FacultyDtoAssertions()));
  }

  static Stream<Arguments> getApiSingleItemErrorsProvider() {
    String apiPath = "/api/faculties/";

    Arguments status400OnStringProvided =
        Arguments.of(
            apiPath + "asdf",
            status().isBadRequest(),
            MediaType.APPLICATION_JSON.toString(),
            "Wrong argument types provided",
            400);

    Arguments status404OnFacultyNotFound =
        Arguments.of(
            apiPath + "999",
            status().isNotFound(),
            MediaType.APPLICATION_JSON.toString(),
            "Faculty with ID: 999 not found",
            404);

    return Stream.of(status400OnStringProvided, status404OnFacultyNotFound);
  }
}
