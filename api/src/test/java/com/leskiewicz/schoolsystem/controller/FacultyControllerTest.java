package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.provider.FacultyTestProvider;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.TestAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
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
        return FacultyTestProvider.getFacultiesCollectionTest(BASE_FACULTIES);
    }

    @Test
    public void getFacultiesTestPagination() throws Exception {
        CommonTests.paginationLinksTest(requestUtils, BASE_FACULTIES, 1);
    }
    //endregion

    //region GetFacultyById
    static Stream<Arguments> getApiSingleItemResponsesProvider() {
        return Stream.of(Arguments.of("/api/faculties/101", status().isOk(), "application/hal+json", FacultyDto.builder().id(101L).name("Informatics").build(), new FacultyDtoAssertions()));
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

    @Test
    public void createFacultyReturns400OnFacultyWithNameAlreadyExists() throws Exception {
        CreateFacultyRequest request = new CreateFacultyRequest("Informatics");
        ResultActions result = requestUtils.performPostRequest(BASE_FACULTIES, request, status().isBadRequest());

        TestAssertions.assertError(result, "Faculty with name: Informatics already exists", BASE_FACULTIES, 400);
    }
     //endregion

    //region UpdateFaculty
    @Test
    public void updateFacultyReturnsCorrectFaculty() throws Exception {
        PatchFacultyRequest request = new PatchFacultyRequest("New Name");
        ResultActions result = requestUtils.performPatchRequest(BASE_FACULTIES + "/101", request, status().isOk());

        FacultyDto expected = FacultyDto.builder().id(101L).name(request.getName()).build();

        // Assert the facultyDto in response body
        facultyDtoAssertions.assertDto(result, expected);
    }

    @Test
    public void updateFacultyReturns404OnFacultyNotFound() throws Exception {
        PatchFacultyRequest request = new PatchFacultyRequest("New Name");
        ResultActions result = requestUtils.performPatchRequest(BASE_FACULTIES + "/999", request, status().isNotFound());

        TestAssertions.assertError(result, "Faculty with ID: 999 not found", BASE_FACULTIES + "/999", 404);
    }

    @Test
    public void updateFacultyReturns400OnFacultyWithNameAlreadyExists() throws Exception {
        PatchFacultyRequest request = new PatchFacultyRequest("Biology");
        ResultActions result = requestUtils.performPatchRequest(BASE_FACULTIES + "/101", request, status().isBadRequest());

        TestAssertions.assertError(result, "Faculty with name: Biology already exists", BASE_FACULTIES + "/101", 400);
    }
    //endregion


}
