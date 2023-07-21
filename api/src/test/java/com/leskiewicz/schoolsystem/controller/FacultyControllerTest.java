package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.testModels.CustomLink;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class FacultyControllerTest extends GenericControllerTest<FacultyDto> {

    private static final String GET_FACULTIES = "/api/faculties";
    private final String GET_FACULTY_BY_ID = "/api/faculties/";

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private MockMvc mvc;

    // Variables
    private ObjectMapper mapper;
    private RequestUtils requestUtils;

    //region GetFaculties tests
    static Stream<Arguments> getApiCollectionResponsesProvider() {
        FacultyDtoAssertions assertions = new FacultyDtoAssertions();
        String facultiesQuery = "http://localhost/api/faculties?page=%d&size=%d&sort=%s&direction=%s";
        FacultyDto informatics = FacultyDto.builder().id(1L).name("Informatics").build();
        FacultyDto biology = FacultyDto.builder().id(2L).name("Biology").build();
        FacultyDto electronics = FacultyDto.builder().id(3L).name("Electronics").build();
        FacultyDto sociology = FacultyDto.builder().id(11L).name("Sociology").build();
        FacultyDto law = FacultyDto.builder().id(12L).name("Law").build();
        FacultyDto economics = FacultyDto.builder().id(13L).name("Economics").build();

        CustomLink selfLink = CustomLink.builder().rel("self").href(String.format(facultiesQuery, 0, 10, "id", "asc")).build();
        CustomLink nextLink = CustomLink.builder().rel("next").href(String.format(facultiesQuery, 1, 10, "id", "asc")).build();
        CustomLink prevLink = CustomLink.builder().rel("prev").href(String.format(facultiesQuery, 0, 10, "id", "asc")).build();
        CustomLink firstLink = CustomLink.builder().rel("first").href(String.format(facultiesQuery, 0, 10, "id", "asc")).build();
        CustomLink lastLink = CustomLink.builder().rel("last").href(String.format(facultiesQuery, 1, 10, "id", "asc")).build();

        Arguments noParams = Arguments.of(GET_FACULTIES, Arrays.asList(informatics, biology, electronics), Arrays.asList(selfLink, nextLink, firstLink, lastLink), assertions);
        Arguments pageOne = Arguments.of(GET_FACULTIES + "?page=1", Arrays.asList(sociology, law, economics), Arrays.asList(selfLink.toBuilder().href(String.format(facultiesQuery, 1, 10, "id", "asc")).build(), prevLink, firstLink, lastLink), assertions);
        Arguments descending = Arguments.of(GET_FACULTIES + "?direction=desc", Arrays.asList(economics, law, sociology), Arrays.asList(selfLink.toBuilder().href(String.format(facultiesQuery, 0, 10, "id", "desc")).build(), nextLink.toBuilder().href(String.format(facultiesQuery, 1, 10, "id", "desc")).build()), assertions);


        return Stream.of(noParams, pageOne, descending);
    }

    //region GetFacultyById
    static Stream<Arguments> getApiSingleItemResponsesProvider() {
        return Stream.of(Arguments.of("/api/faculties/1", status().isOk(), "application/hal+json", FacultyDto.builder().id(1L).name("Informatics").build(), new FacultyDtoAssertions()));
    }
    //endregion

    static Stream<Arguments> getApiSingleItemErrorsProvider() {
        String apiPath = "/api/faculties/";

        Arguments status400OnStringProvided = Arguments.of(apiPath + "asdf", status().isBadRequest(), MediaType.APPLICATION_JSON.toString(), "Wrong argument types provided", 400);

        Arguments status404OnFacultyNotFound = Arguments.of(apiPath + "999", status().isNotFound(), MediaType.APPLICATION_JSON.toString(), "Faculty with ID: 999 not found", 404);

        return Stream.of(status400OnStringProvided, status404OnFacultyNotFound);
    }

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());
        requestUtils = new RequestUtilsImpl(mvc, mapper);

    }
    //endregion


}
