package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.testModels.CustomLink;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.TestAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import org.junit.jupiter.api.Assertions;
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

import java.util.Arrays;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class FacultyControllerTest extends GenericControllerTest<FacultyDto> {

    private static final String BASE_FACULTIES = "/api/faculties";
    private final String GET_FACULTY_BY_ID = "/api/faculties/";

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private MockMvc mvc;

    // Variables
    private ObjectMapper mapper;
    private RequestUtils requestUtils;
    private FacultyDtoAssertions facultyDtoAssertions;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());
        requestUtils = new RequestUtilsImpl(mvc, mapper);
        facultyDtoAssertions = new FacultyDtoAssertions();
    }


    //region GetFaculties tests
    static Stream<Arguments> getApiCollectionResponsesProvider() {
        FacultyDtoAssertions assertions = new FacultyDtoAssertions();
        String facultiesQuery = "http://localhost/api/faculties?page=%d&size=%d&sort=%s&direction=%s";
        FacultyDto informatics = FacultyDto.builder().id(1L).name("Informatics").build();
        FacultyDto biology = FacultyDto.builder().id(2L).name("Biology").build();
        FacultyDto electronics = FacultyDto.builder().id(3L).name("Electronics").build();
        FacultyDto chemistry = FacultyDto.builder().id(4L).name("Chemistry").build();
        FacultyDto sociology = FacultyDto.builder().id(11L).name("Sociology").build();
        FacultyDto law = FacultyDto.builder().id(12L).name("Law").build();
        FacultyDto economics = FacultyDto.builder().id(13L).name("Economics").build();

        CustomLink selfLink = CustomLink.builder().rel("self").href(String.format(facultiesQuery, 0, 10, "id", "asc")).build();
        CustomLink nextLink = CustomLink.builder().rel("next").href(String.format(facultiesQuery, 1, 10, "id", "asc")).build();
        CustomLink prevLink = CustomLink.builder().rel("prev").href(String.format(facultiesQuery, 0, 10, "id", "asc")).build();
        CustomLink firstLink = CustomLink.builder().rel("first").href(String.format(facultiesQuery, 0, 10, "id", "asc")).build();
        CustomLink lastLink = CustomLink.builder().rel("last").href(String.format(facultiesQuery, 1, 10, "id", "asc")).build();

        Arguments noParams = Arguments.of(BASE_FACULTIES, Arrays.asList(informatics, biology, electronics), Arrays.asList(selfLink, nextLink, firstLink, lastLink), assertions);
        Arguments pageOne = Arguments.of(BASE_FACULTIES + "?page=1", Arrays.asList(sociology, law, economics), Arrays.asList(selfLink.toBuilder().href(String.format(facultiesQuery, 1, 10, "id", "asc")).build(), prevLink, firstLink, lastLink), assertions);
        Arguments descending = Arguments.of(BASE_FACULTIES + "?direction=desc", Arrays.asList(economics, law, sociology), Arrays.asList(selfLink.toBuilder().href(String.format(facultiesQuery, 0, 10, "id", "desc")).build(), nextLink.toBuilder().href(String.format(facultiesQuery, 1, 10, "id", "desc")).build()), assertions);
        Arguments sortByName = Arguments.of(BASE_FACULTIES + "?sort=name", Arrays.asList(biology, chemistry), Arrays.asList(selfLink.toBuilder().href(String.format(facultiesQuery, 0, 10, "name", "asc")).build(), nextLink.toBuilder().href(String.format(facultiesQuery, 1, 10, "name", "asc")).build()), assertions);
        Arguments pageSize20 = Arguments.of(BASE_FACULTIES + "?size=20", Arrays.asList(informatics, biology, electronics, chemistry), Arrays.asList(selfLink.toBuilder().href(String.format(facultiesQuery, 0, 20, "id", "asc")).build(), firstLink.toBuilder().href(String.format(facultiesQuery, 0, 20, "id", "asc")).build(), lastLink.toBuilder().href(String.format(facultiesQuery, 0, 20, "id", "asc")).build()), assertions);

        return Stream.of(noParams, pageOne, descending, sortByName, pageSize20);
    }
    //endregion

    //region GetFacultyById
    static Stream<Arguments> getApiSingleItemResponsesProvider() {
        return Stream.of(Arguments.of("/api/faculties/1", status().isOk(), "application/hal+json", FacultyDto.builder().id(1L).name("Informatics").build(), new FacultyDtoAssertions()));
    }

    static Stream<Arguments> getApiSingleItemErrorsProvider() {
        String apiPath = "/api/faculties/";

        Arguments status400OnStringProvided = Arguments.of(apiPath + "asdf", status().isBadRequest(), MediaType.APPLICATION_JSON.toString(), "Wrong argument types provided", 400);

        Arguments status404OnFacultyNotFound = Arguments.of(apiPath + "999", status().isNotFound(), MediaType.APPLICATION_JSON.toString(), "Faculty with ID: 999 not found", 404);

        return Stream.of(status400OnStringProvided, status404OnFacultyNotFound);
    }
    //endregion

    //region CreateFaculty
    @Test
    public void createFacultyReturnsCorrectFacultyAndLocationHeader() throws Exception {
        CreateFacultyRequest request = new CreateFacultyRequest("Testfaculty");
        ResultActions result = requestUtils.performPostRequest(BASE_FACULTIES, request, status().isCreated());

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
        ResultActions result = requestUtils.performPostRequest(BASE_FACULTIES, request, status().isBadRequest());

        TestAssertions.assertError(result, "Faculty name required", BASE_FACULTIES, 400);
    }
    //endregion




}
