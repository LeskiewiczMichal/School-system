package com.leskiewicz.schoolsystem.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.ApiError;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.testUtils.assertions.TestAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.UserDtoAssertions;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PatchUserApiTest {

  private final String GET_USER_BY_ID = "/api/users/";
  private final UserDtoAssertions userDtoAssertions = new UserDtoAssertions();
  @Autowired private MockMvc mvc;
  @Autowired private UserRepository userRepository;
  @Autowired private DegreeRepository degreeRepository;
  @Autowired private FacultyRepository facultyRepository;

  // Variables
  private ObjectMapper mapper;
  private RequestUtils requestUtils;
  Faculty faculty;
  Degree degree;
  User user;

  static Stream<Arguments> patchUserHappyPathProvider() {
    Faculty faculty = TestHelper.createFaculty();
    Degree degree = TestHelper.createDegree(faculty);
    UserDto baseUser = TestHelper.createUserDto(faculty, degree);

    Arguments changeName =
            Arguments.of(
                    PatchUserRequest.builder().firstName("Michal").build(),
                    baseUser.toBuilder().firstName("Michal").build());

    baseUser = baseUser.toBuilder().firstName("Michal").build();
    Arguments changeLastName =
            Arguments.of(
                    PatchUserRequest.builder().lastName("Leskiewicz").build(),
                    baseUser.toBuilder().lastName("Leskiewicz").build());

    baseUser = baseUser.toBuilder().lastName("Leskiewicz").build();
    Arguments changeEmail =
            Arguments.of(
                    PatchUserRequest.builder().email("test@example.com").build(),
                    baseUser.toBuilder().email("test@example.com").build());

    baseUser = baseUser.toBuilder().email("test@example.com").build();
    Arguments changeDegree =
            Arguments.of(
                    PatchUserRequest.builder()
                            .degreeField("Software Engineering")
                            .degreeTitle(DegreeTitle.MASTER)
                            .build(),
                    baseUser.toBuilder().degree("Software Engineering").build());

    baseUser = baseUser.toBuilder().degree("Software Engineering").build();
    Arguments changeFaculty =
            Arguments.of(
                    PatchUserRequest.builder().facultyName("Biology").build(),
                    baseUser.toBuilder().faculty("Biology").build());

    baseUser = baseUser.toBuilder().faculty("Biology").build();
    Arguments changeFacultyAndDegree =
            Arguments.of(
                    PatchUserRequest.builder()
                            .facultyName("Biology")
                            .degreeTitle(DegreeTitle.BACHELOR)
                            .degreeField("Nano")
                            .build(),
                    baseUser.toBuilder().faculty("Biology").degree("Nano").build());

    return Stream.of(
            changeName,
            changeLastName,
            changeEmail,
            changeDegree,
            changeFaculty,
            changeFacultyAndDegree);
  }

  static Stream<Arguments> patchUserErrorTestingProvider() {
    String path = "/api/users/1";

    Arguments changeDegreeToNotCorrectOnCurrentFaculty =
            Arguments.of(
                    PatchUserRequest.builder()
                            .degreeField("Nano")
                            .degreeTitle(DegreeTitle.BACHELOR)
                            .build(),
                    new ApiError(
                            path,
                            ErrorMessages.objectWasNotUpdated("User")
                                    + ". "
                                    + ErrorMessages.degreeNotOnFaculty("Nano", DegreeTitle.BACHELOR, "TestFaculty"),
                            404,
                            LocalDateTime.now()),
                    status().isNotFound());

    Arguments changeFacultyToNonExistent =
            Arguments.of(
                    PatchUserRequest.builder().facultyName("qwre").build(),
                    new ApiError(
                            path,
                            ErrorMessages.objectWasNotUpdated("User")
                                    + ". "
                                    + ErrorMessages.objectWithNameNotFound("Faculty", "qwre"),
                            404,
                            LocalDateTime.now()),
                    status().isNotFound());

    Arguments changeFacultyToOneThatHaveNotGotTheSameDegree =
            Arguments.of(
                    PatchUserRequest.builder().facultyName("Electronics").build(),
                    new ApiError(
                            path,
                            ErrorMessages.objectWasNotUpdated("User")
                                    + ". "
                                    + ErrorMessages.degreeNotOnFaculty(
                                    "Computer Science", DegreeTitle.BACHELOR_OF_SCIENCE, "Electronics"),
                            404,
                            LocalDateTime.now()),
                    status().isNotFound());

    Arguments changeFacultyAndDegreeButDegreeButDegreeIsNotOnNewFaculty =
            Arguments.of(
                    PatchUserRequest.builder()
                            .facultyName("Electronics")
                            .degreeTitle(DegreeTitle.BACHELOR)
                            .degreeField("Nano")
                            .build(),
                    new ApiError(
                            path,
                            ErrorMessages.objectWasNotUpdated("User")
                                    + ". "
                                    + ErrorMessages.degreeNotOnFaculty("Nano", DegreeTitle.BACHELOR, "Electronics"),
                            404,
                            LocalDateTime.now()),
                    status().isNotFound());

    return Stream.of(
            changeDegreeToNotCorrectOnCurrentFaculty,
            changeFacultyToNonExistent,
            changeFacultyToOneThatHaveNotGotTheSameDegree,
            changeFacultyAndDegreeButDegreeButDegreeIsNotOnNewFaculty);
  }

  @BeforeAll
  public void setUp() {
    // Configure object mapper
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    requestUtils = new RequestUtilsImpl(mvc, mapper);

    // Create user to modify
    faculty = TestHelper.createFaculty();
    degree = TestHelper.createDegree(faculty);
    user = TestHelper.createUser(faculty, degree);
    faculty.setId(null);
    degree.setId(null);
    user.setId(null);

    facultyRepository.save(faculty);
    degreeRepository.save(degree);
    userRepository.save(user);

    Degree softwareEngineeringMaster =
        Degree.builder().faculty(faculty).fieldOfStudy("Software Engineering").title(DegreeTitle.MASTER).build();
    Faculty biology = Faculty.builder().name("Biology").degree(degree).build();
    Degree softwareEngineeringMasterBiology =
            Degree.builder().faculty(biology).fieldOfStudy("Software Engineering").title(DegreeTitle.MASTER).build();
    Degree bachelorOfNanoOnBiology = Degree.builder().faculty(biology).fieldOfStudy("Nano").title(DegreeTitle.BACHELOR).build();
    Degree bachelorOfNanoOnTestFaculty = Degree.builder().faculty(faculty).fieldOfStudy("Nano").title(DegreeTitle.BACHELOR).build();
    Faculty electronics = Faculty.builder().name("Electronics").degree(softwareEngineeringMaster).build();

    degreeRepository.save(softwareEngineeringMaster);
    facultyRepository.save(biology);
    degreeRepository.save(softwareEngineeringMasterBiology);
    degreeRepository.save(bachelorOfNanoOnBiology);
//    degreeRepository.save(bachelorOfNanoOnTestFaculty);
    facultyRepository.save(electronics);
  }

  @DisplayName("Patch user API with different params")
  @ParameterizedTest
  @MethodSource("patchUserHappyPathProvider")
  public void patchUserHappyPath(PatchUserRequest request, UserDto expectedUser) throws Exception {
    ResultActions result =
            requestUtils.performPatchRequest(GET_USER_BY_ID + "1", request, status().isOk());

    userDtoAssertions.assertDto(result, expectedUser);
  }

  @Test
  public void patchUserReturnsStatus404OnUserNotFound() throws Exception {
    ResultActions result =
            requestUtils.performPatchRequest(
                    GET_USER_BY_ID + "300",
                    PatchUserRequest.builder().firstName("Ok").build(),
                    status().isNotFound());

    TestAssertions.assertError(
            result, ErrorMessages.objectWithIdNotFound("User", 300L), GET_USER_BY_ID + "300", 404);
  }

  @ParameterizedTest
  @MethodSource("patchUserErrorTestingProvider")
  public void patchUserErrorTesting(
          PatchUserRequest request, ApiError expectedError, ResultMatcher expectedStatus)
          throws Exception {
    ResultActions result =
            requestUtils.performPatchRequest(GET_USER_BY_ID + "1", request, expectedStatus);

    TestAssertions.assertError(
            result, expectedError.message(), expectedError.path(), expectedError.statusCode());
  }
}
