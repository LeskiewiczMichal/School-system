package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.testModels.CustomLink;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class FacultyControllerTest extends GenericControllerTest<UserDto> {

  private final String GET_FACULTIES = "/api/faculties";
  private final String GET_FACULTY_BY_ID = "/api/faculties/";

//  @Autowired
//  private MockMvc mvc;

  @Autowired
  private FacultyRepository facultyRepository;


  // Variables
//  Faculty
//
//  @BeforeAll
//  public void queryObjects() {
//
//  }

  // Variables

//  private RequestUtils requestUtils;

//  @BeforeEach
//  public void setup() {
//    requestUtils = new RequestUtilsImpl(mvc,
//        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//            .registerModule(new JavaTimeModule()));
//  }

  //region GetFaculties tests
//  @DisplayName("Get faculties API returns correct responses with different params")
//  @ParameterizedTest
//  @MethodSource("getFacultiesHappyPathProvider")
//  public void getFacultiesHappyPath(String queryString, List<FacultyDto> faculties,
//      List<CustomLink> links) throws Exception {
//    ResultActions result = requestUtils.performGetRequest(GET_FACULTIES + queryString, status().isOk());
//
//    for (int i = 0; i < faculties.size(); i++) {
//      FacultyDto
//    }
//
//  }
  static Stream<Arguments> getHappyPathProvider() {
    FacultyDto facultyDto = FacultyDto.builder()
        .id(1L)
        .name("")

    Arguments arguments = Arguments.of(

    )
  }
  //endregion
}
