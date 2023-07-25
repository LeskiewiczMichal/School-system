package com.leskiewicz.schoolsystem.faculty.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.TestAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CreateFacultyApiTest {

  private static final String BASE_FACULTIES = "/api/faculties";

  @Autowired private FacultyRepository facultyRepository;
  @Autowired private MockMvc mvc;

  private ObjectMapper mapper;
  private RequestUtils requestUtils;
  private FacultyDtoAssertions facultyDtoAssertions;

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
  public void createFacultyReturnsCorrectFacultyAndLocationHeader() throws Exception {
    CreateFacultyRequest request = new CreateFacultyRequest("Testfaculty");

    // Call endpoint
    ResultActions result =
        requestUtils.performPostRequest(BASE_FACULTIES, request, status().isCreated());

    // Create expected facultyDto
    FacultyDto expected = FacultyDto.builder().id(1L).name(request.getName()).build();

    // Assert the facultyDto in response body
    facultyDtoAssertions.assertDtoWithAnyId(result, expected);

    // Assert correct location header
    result.andExpect(MockMvcResultMatchers.header().exists("Location"));
  }

  @Test
  public void createFacultyReturns400OnEmptyName() throws Exception {
    CreateFacultyRequest request = new CreateFacultyRequest();

    // Call endpoint
    ResultActions result =
        requestUtils.performPostRequest(BASE_FACULTIES, request, status().isBadRequest());

    TestAssertions.assertError(result, "Faculty name required", BASE_FACULTIES, 400);
  }

  @Test
  public void createFacultyReturns400OnFacultyWithNameAlreadyExists() throws Exception {
    // Save the same faculty that we want to create
    Faculty faculty = Faculty.builder().name("zxcv").build();
    facultyRepository.save(faculty);

    // Create request for that same faculty we saved earlier
    CreateFacultyRequest request = new CreateFacultyRequest(faculty.getName());

    // Call endpoint
    ResultActions result =
        requestUtils.performPostRequest(BASE_FACULTIES, request, status().isBadRequest());

    // Assert name was already taken
    TestAssertions.assertError(
        result, "Faculty with name: zxcv already exists", BASE_FACULTIES, 400);
  }
}
