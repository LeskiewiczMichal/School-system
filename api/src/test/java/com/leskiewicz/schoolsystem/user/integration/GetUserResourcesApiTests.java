package com.leskiewicz.schoolsystem.user.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.assertions.CourseDtoAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class GetUserResourcesApiTests {

  private static final String BASE_PATH = "/api/users";

  @Autowired private MockMvc mvc;

  // Variables
  private ObjectMapper mapper;
  private RequestUtils requestUtils;
  private CourseDtoAssertions courseDtoAssertions = new CourseDtoAssertions();

  @BeforeEach
  public void setUp() {
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    requestUtils = new RequestUtilsImpl(mvc, mapper);
  }

  // *** GetUserCourses *** //

  @Test
  public void getUserCoursesReturnsCorrectCourses() throws Exception {
    ResultActions result =
        requestUtils.performGetRequest(BASE_PATH + "/1/courses", status().isOk());

    CourseDto expectedCourse =
        CourseDto.builder()
            .id(1L)
            .title("Introduction to Programming")
            .teacher("Olivia Martinez")
            .faculty("Informatics")
            .durationInHours(40)
            .teacherId(106L)
            .facultyId(101L)
            .build();
    courseDtoAssertions.assertDtoInCollection(result, 0, expectedCourse);
  }
}
