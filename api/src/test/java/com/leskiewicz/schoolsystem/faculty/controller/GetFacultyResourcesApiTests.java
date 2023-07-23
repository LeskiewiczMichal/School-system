package com.leskiewicz.schoolsystem.faculty.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.testUtils.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
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
public class GetFacultyResourcesApiTests {

  private static final String BASE_FACULTIES = "/api/faculties";

  @Autowired private MockMvc mvc;

  // Variables
  private ObjectMapper mapper;
  private RequestUtils requestUtils;

  @BeforeEach
  public void setUp() {
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    requestUtils = new RequestUtilsImpl(mvc, mapper);
  }

  // *** GetFacultyStudents and Teachers ***//

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

  // *** GetFacultyDegrees ***//

  @Test
  public void getFacultyDegreesReturnsCorrectStudents() throws Exception {
    ResultActions result =
        requestUtils.performGetRequest(BASE_FACULTIES + "/101/degrees", status().isOk());

    result
        .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.degrees").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.degrees").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.degrees").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.degrees[0].id").value(101))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.degrees[0].title")
                .value("BACHELOR_OF_SCIENCE"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.degrees[0].fieldOfStudy")
                .value("Computer Science"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.degrees[1].id").value(102))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.degrees[1].title").value("MASTER"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.degrees[1].fieldOfStudy")
                .value("Software Engineering"));
  }

  @Test
  public void getFacultyDegreesTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, BASE_FACULTIES + "/101/degrees", 0);
  }
}
