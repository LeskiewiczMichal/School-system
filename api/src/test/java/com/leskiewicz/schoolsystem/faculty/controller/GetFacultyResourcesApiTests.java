package com.leskiewicz.schoolsystem.faculty.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testModels.DegreeDto;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.assertions.CourseDtoAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.DegreeDtoAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.UserDtoAssertions;
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

  // Assertions
  private final CourseDtoAssertions courseDtoAssertions = new CourseDtoAssertions();
  private final DegreeDtoAssertions degreeDtoAssertions = new DegreeDtoAssertions();
  private final UserDtoAssertions userDtoAssertions = new UserDtoAssertions();

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

    UserDto userDto =
        UserDto.builder()
            .id(6L)
            .firstName("Olivia")
            .lastName("Martinez")
            .email("olivia.martinez@example.com")
            .faculty("Mathematics")
            .build();

    userDtoAssertions.assertDtoInCollection(result, 0, userDto);
  }

  @Test
  public void getFacultyUsersAndGetFacultyTeachersTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, BASE_FACULTIES + "/101/students", 0);
    CommonTests.paginationLinksTest(requestUtils, BASE_FACULTIES + "/101/teachers", 0);
  }

  // *** GetFacultyDegrees ***//

  @Test
  public void getFacultyDegreesReturnsCorrectDegrees() throws Exception {
    ResultActions result =
        requestUtils.performGetRequest(BASE_FACULTIES + "/101/degrees", status().isOk());

    DegreeDto degreeDto =
        DegreeDto.builder()
            .id(101L)
            .title(DegreeTitle.BACHELOR_OF_SCIENCE)
            .fieldOfStudy("Computer Science")
            .faculty("Informatics")
            .build();
    DegreeDto degreeDto2 =
        DegreeDto.builder()
            .id(102L)
            .title(DegreeTitle.MASTER)
            .fieldOfStudy("Software Engineering")
            .faculty("Informatics")
            .build();

    degreeDtoAssertions.assertDtoInCollection(result, 0, degreeDto);
    degreeDtoAssertions.assertDtoInCollection(result, 1, degreeDto2);
  }

  // *** GetFacultyCourses *** //

  @Test
  public void getFacultyCoursesReturnsCorrectCourses() throws Exception {
    ResultActions result =
        requestUtils.performGetRequest(BASE_FACULTIES + "/101/courses", status().isOk());

    CourseDto courseDto =
        CourseDto.builder()
            .id(1L)
            .title("Introduction to Programming")
            .durationInHours(40)
            .teacher("Olivia Martinez")
            .faculty("Informatics")
            .build();

    courseDtoAssertions.assertDtoInCollection(result, 0, courseDto);
  }

  @Test
  public void getFacultyDegreesTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, BASE_FACULTIES + "/101/degrees", 0);
  }
}
