//package com.leskiewicz.schoolsystem.degree.integration;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.leskiewicz.schoolsystem.degree.Degree;
//import com.leskiewicz.schoolsystem.degree.DegreeRepository;
//import com.leskiewicz.schoolsystem.degree.DegreeTitle;
//import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
//import com.leskiewicz.schoolsystem.faculty.Faculty;
//import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
//import com.leskiewicz.schoolsystem.testModels.DegreeDto;
//import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
//import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
//import com.leskiewicz.schoolsystem.testUtils.assertions.DegreeDtoAssertions;
//import com.leskiewicz.schoolsystem.testUtils.assertions.TestAssertions;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class CreateDegreeApiTest {
//
//  private static final String BASE_URL = "/api/degrees";
//
//  DegreeDtoAssertions degreeDtoAssertions = new DegreeDtoAssertions();
//  Faculty faculty;
//  @Autowired private MockMvc mvc;
//  @Autowired private FacultyRepository facultyRepository;
//  @Autowired private DegreeRepository degreeRepository;
//  // Variables
//  private ObjectMapper mapper;
//  private RequestUtils requestUtils;
//
//  static Stream<Arguments> createDegreeReturns400OnWrongRequestProvider() {
//    Arguments nullTitle =
//        Arguments.of(new CreateDegreeRequest(null, "Test", "Law"), "Degree title required");
//
//    Arguments nullFieldOfStudy =
//        Arguments.of(
//            new CreateDegreeRequest(DegreeTitle.BACHELOR, null, "Law"),
//            "Degree field of study required");
//
//    Arguments nullFaculty =
//        Arguments.of(
//            new CreateDegreeRequest(DegreeTitle.BACHELOR, "Test", null), "Faculty name required");
//
//    return Stream.of(nullTitle, nullFieldOfStudy, nullFaculty);
//  }
//
//  @BeforeEach
//  public void setup() {
//    mapper =
//        new ObjectMapper()
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//            .registerModule(new JavaTimeModule());
//    requestUtils = new RequestUtilsImpl(mvc, mapper);
//
//    // Create faculty to query
//    faculty = Faculty.builder().name("Test").build();
//    facultyRepository.save(faculty);
//  }
//
//  @Test
//  public void createDegreeReturnsCorrectDegreeAndLocationHeader() throws Exception {
//    CreateDegreeRequest request = new CreateDegreeRequest(DegreeTitle.BACHELOR, "Test", "Test");
//
//    // Call the endpoint
//    ResultActions result = requestUtils.performPostRequest(BASE_URL, request, status().isCreated());
//
//    // Create expected degreeDto
//    DegreeDto expected =
//        DegreeDto.builder()
//            .id(1L)
//            .title(DegreeTitle.BACHELOR)
//            .fieldOfStudy(request.getFieldOfStudy())
//            .faculty(request.getFacultyName())
//            .build();
//
//    // Assert degreeDto in response body
//    degreeDtoAssertions.assertDtoWithAnyId(result, expected);
//
//    // Assert location header exists
//    result.andExpect(MockMvcResultMatchers.header().exists("Location"));
//  }
//
//  @ParameterizedTest
//  @MethodSource("createDegreeReturns400OnWrongRequestProvider")
//  public void createDegreeReturns400OnWrongRequest(
//      CreateDegreeRequest request, String expectedMessage) throws Exception {
//
//    ResultActions result =
//        requestUtils.performPostRequest(BASE_URL, request, status().isBadRequest());
//
//    TestAssertions.assertError(result, expectedMessage, BASE_URL, 400);
//  }
//
//  @Test
//  public void createDegreeReturns400OnDegreeWithAlreadyExists() throws Exception {
//    // Save the same degree that we want to create
//    Degree degree =
//        Degree.builder()
//            .title(DegreeTitle.BACHELOR_OF_SCIENCE)
//            .fieldOfStudy("Computer Science")
//            .faculty(faculty)
//            .build();
//    degreeRepository.save(degree);
//
//    // Create request for degree that we created earlier
//    CreateDegreeRequest request =
//        new CreateDegreeRequest(
//            degree.getTitle(), degree.getFieldOfStudy(), degree.getFaculty().getName());
//
//    // Call the endpoint
//    ResultActions result =
//        requestUtils.performPostRequest(BASE_URL, request, status().isBadRequest());
//
//    TestAssertions.assertError(
//        result,
//        "Degree with title: BACHELOR_OF_SCIENCE in Computer Science on faculty: Test already exists",
//        BASE_URL,
//        400);
//  }
//}
