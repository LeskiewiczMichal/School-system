package com.leskiewicz.schoolsystem.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.TestAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
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
public class FacultyControllerTest extends GenericControllerTest<FacultyDto> {

  private static final String BASE_FACULTIES = "/api/faculties";
  private final String GET_FACULTY_BY_ID = "/api/faculties/";

  @Autowired private FacultyRepository facultyRepository;

  @Autowired private MockMvc mvc;

  // Variables
  private ObjectMapper mapper;
  private RequestUtils requestUtils;
  private FacultyDtoAssertions facultyDtoAssertions;

  // region GetFaculties tests
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
            BASE_FACULTIES + "?direction=desc",
            Arrays.asList(economics, law, sociology),
            assertions);
    Arguments sortByName =
        Arguments.of(BASE_FACULTIES + "?sort=name", Arrays.asList(biology, chemistry), assertions);
    Arguments pageSize20 =
        Arguments.of(
            BASE_FACULTIES + "?size=20",
            Arrays.asList(informatics, biology, electronics, chemistry),
            assertions);

    return Stream.of(noParams, pageOne, descending, sortByName, pageSize20);
  }

  // region GetFacultyById
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
  // endregion

  @BeforeEach
  public void setUp() {
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    requestUtils = new RequestUtilsImpl(mvc, mapper);
    facultyDtoAssertions = new FacultyDtoAssertions();
  }

  @Test
  public void getFacultiesTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, BASE_FACULTIES, 1);
  }
  // endregion

  // region CreateFaculty
  @Test
  public void createFacultyReturnsCorrectFacultyAndLocationHeader() throws Exception {
    CreateFacultyRequest request = new CreateFacultyRequest("Testfaculty");
    ResultActions result =
        requestUtils.performPostRequest(BASE_FACULTIES, request, status().isCreated());

    FacultyDto expected = FacultyDto.builder().id(1L).name(request.getName()).build();

    // Assert the facultyDto in response body
    facultyDtoAssertions.assertDto(result, expected);

    // Assert correct location header
    String expectedLocation = "http://localhost/api/faculties/1";
    result.andExpect(MockMvcResultMatchers.header().string("Location", expectedLocation));
  }

  @Test
  public void createFacultyReturns400OnEmptyName() throws Exception {
    CreateFacultyRequest request = new CreateFacultyRequest();
    ResultActions result =
        requestUtils.performPostRequest(BASE_FACULTIES, request, status().isBadRequest());

    TestAssertions.assertError(result, "Faculty name required", BASE_FACULTIES, 400);
  }

  @Test
  public void createFacultyReturns400OnFacultyWithNameAlreadyExists() throws Exception {
    CreateFacultyRequest request = new CreateFacultyRequest("Informatics");
    ResultActions result =
        requestUtils.performPostRequest(BASE_FACULTIES, request, status().isBadRequest());

    TestAssertions.assertError(
        result, "Faculty with name: Informatics already exists", BASE_FACULTIES, 400);
  }
  // endregion

  // region UpdateFaculty
  @Test
  public void updateFacultyReturnsCorrectFaculty() throws Exception {
    PatchFacultyRequest request = new PatchFacultyRequest("New Name");
    ResultActions result =
        requestUtils.performPatchRequest(BASE_FACULTIES + "/101", request, status().isOk());

    FacultyDto expected = FacultyDto.builder().id(101L).name(request.getName()).build();

    // Assert the facultyDto in response body
    facultyDtoAssertions.assertDto(result, expected);
  }

  @Test
  public void updateFacultyReturns404OnFacultyNotFound() throws Exception {
    PatchFacultyRequest request = new PatchFacultyRequest("New Name");
    ResultActions result =
        requestUtils.performPatchRequest(BASE_FACULTIES + "/999", request, status().isNotFound());

    TestAssertions.assertError(
        result, "Faculty with ID: 999 not found", BASE_FACULTIES + "/999", 404);
  }

  @Test
  public void updateFacultyReturns400OnFacultyWithNameAlreadyExists() throws Exception {
    PatchFacultyRequest request = new PatchFacultyRequest("Biology");
    ResultActions result =
        requestUtils.performPatchRequest(BASE_FACULTIES + "/101", request, status().isBadRequest());

    TestAssertions.assertError(
        result, "Faculty with name: Biology already exists", BASE_FACULTIES + "/101", 400);
  }
  // endregion

  // region GetFacultyStudents and GetFacultyTeachers tests
  @Test
  public void getFacultyStudentsReturnsCorrectStudents() throws Exception {
    ResultActions result =
        requestUtils.performGetRequest(BASE_FACULTIES + "/101/students", status().isOk());

    result
        .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users[0].firstName").value("John"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users[0].lastName").value("Doe"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.users[0].email")
                .value("johndoe@example.com"));
  }

  @Test
  public void getFacultyTeachersReturnsCorrectTeachers() throws Exception {
    ResultActions result =
        requestUtils.performGetRequest(BASE_FACULTIES + "/106/teachers", status().isOk());

    result
        .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users[0].id").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.users[0].firstName").value("Olivia"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.users[0].lastName").value("Martinez"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.users[0].email")
                .value("olivia.martinez@example.com"));
  }

  @Test
  public void getFacultyUsersAndGetFacultyTeachersTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, BASE_FACULTIES + "/101/students", 0);
    CommonTests.paginationLinksTest(requestUtils, BASE_FACULTIES + "/101/teachers", 0);
  }
  // endregion
}
