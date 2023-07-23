package com.leskiewicz.schoolsystem.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.ApiError;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import com.leskiewicz.schoolsystem.testUtils.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.TestAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.UserDtoAssertions;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:usersTest.sql"})
public class UserControllerTest extends GenericControllerTest<UserDto> {

  private final String GET_USERS_PATH = "/api/users";
  private final String GET_USER_BY_ID = "/api/users/";
//  private final String GET_USER_FACULTY = "/api/users/%d/faculty";

  @Autowired private MockMvc mvc;
  @Autowired private UserRepository userRepository;

  // Variables
  private ObjectMapper mapper;
  private RequestUtils requestUtils;
//  private FacultyDtoAssertions facultyDtoAssertions = new FacultyDtoAssertions();

  //region Providers
  static Stream<Arguments> getApiCollectionResponsesProvider() {
    UserDto baseUser =
        UserDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("johndoe@example.com")
            .faculty("Informatics")
            .degree("Computer Science")
            .build();

    UserDtoAssertions userDtoAssertions = new UserDtoAssertions();

    Arguments noParams =
        Arguments.of(
            "/api/users",
            Arrays.asList(
                baseUser.toBuilder().build(),
                baseUser.toBuilder()
                    .id(2L)
                    .firstName("Alice")
                    .lastName("Smith")
                    .email("alicesmith@example.com")
                    .build(),
                baseUser.toBuilder()
                    .id(3L)
                    .firstName("Bob")
                    .lastName("Johnson")
                    .email("bobjohnson@example.com")
                    .degree(null)
                    .build()),
            userDtoAssertions);
    Arguments pageOne =
        Arguments.of(
            "/api/users?page=1",
            Arrays.asList(
                baseUser.toBuilder()
                    .id(11L)
                    .firstName("Alice")
                    .lastName("Smith")
                    .email("alicesmith@example.com")
                    .build(),
                baseUser.toBuilder()
                    .id(12L)
                    .firstName("Alice")
                    .lastName("Smith")
                    .email("alicesmith@example.com")
                    .build()),
            userDtoAssertions);
    Arguments descending =
        Arguments.of(
            "/api/users?sort=id,desc",
            Arrays.asList(
                baseUser.toBuilder()
                    .id(26L)
                    .firstName("Abba")
                    .lastName("Smith")
                    .email("alicesmith@example.com")
                    .build(),
                baseUser.toBuilder()
                    .id(25L)
                    .firstName("Alice")
                    .lastName("Smith")
                    .email("alicesmith@example.com")
                    .build()),
            userDtoAssertions);
    Arguments sortByName =
        Arguments.of(
            "/api/users?sort=firstName",
            Arrays.asList(
                baseUser.toBuilder()
                    .id(26L)
                    .firstName("Abba")
                    .lastName("Smith")
                    .email("alicesmith@example.com")
                    .build()),
            userDtoAssertions);
    Arguments pageSize20 = Arguments.of("/api/users?size=20", Arrays.asList(), userDtoAssertions);

    return Stream.of(noParams, pageOne, descending, sortByName, pageSize20);
  }

  static Stream<Arguments> getApiSingleItemResponsesProvider() {
    Arguments happyPath =
        Arguments.of(
            "/api/users/1",
            status().isOk(),
            "application/hal+json",
            new UserDto(
                1L, "John", "Doe", "johndoe@example.com", "Informatics", "Computer Science"),
            new UserDtoAssertions());

    return Stream.of(happyPath);
  }

  static Stream<Arguments> getApiSingleItemErrorsProvider() {
    String apiPath = "/api/users/";

    Arguments status400OnStringProvided =
        Arguments.of(
            apiPath + "asdf",
            status().isBadRequest(),
            MediaType.APPLICATION_JSON.toString(),
            "Wrong argument types provided",
            400);

    Arguments status404OnUserNotFound =
        Arguments.of(
            apiPath + "9999",
            status().isNotFound(),
            MediaType.APPLICATION_JSON.toString(),
            ErrorMessages.objectWithIdNotFound("User", 9999L),
            404);

    return Stream.of(status400OnStringProvided, status404OnUserNotFound);
  }

  static Stream<Arguments> patchUserHappyPathProvider() {
    UserDto baseUser =
        UserDto.builder()
            .id(20L)
            .firstName("Alice")
            .lastName("Smith")
            .email("alicesmith@example.com")
            .faculty("Informatics")
            .degree("Computer Science")
            .build();

    Arguments changeName =
        Arguments.of(
            PatchUserRequest.builder().firstName("Michal").build(),
            baseUser.toBuilder().firstName("Michal").build());

    Arguments changeLastName =
        Arguments.of(
            PatchUserRequest.builder().lastName("Leskiewicz").build(),
            baseUser.toBuilder().lastName("Leskiewicz").build());

    Arguments changeEmail =
        Arguments.of(
            PatchUserRequest.builder().email("test@example.com").build(),
            baseUser.toBuilder().email("test@example.com").build());

    Arguments changeDegree =
        Arguments.of(
            PatchUserRequest.builder()
                .degreeField("Software Engineering")
                .degreeTitle(DegreeTitle.MASTER)
                .build(),
            baseUser.toBuilder().degree("Software Engineering").build());

    Arguments changeFaculty =
        Arguments.of(
            PatchUserRequest.builder().facultyName("Biology").build(),
            baseUser.toBuilder().faculty("Biology").build());

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
    String path = "/api/users/20";

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
                    + ErrorMessages.degreeNotOnFaculty("Nano", DegreeTitle.BACHELOR, "Informatics"),
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
  // endregion

  @BeforeEach
  public void setUp() {
    // Configure object mapper
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    requestUtils = new RequestUtilsImpl(mvc, mapper);
  }

  @Test
  public void getUsersTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, GET_USERS_PATH, 2);
  }

  // region PatchUser tests
  @DisplayName("Patch user API with different params")
  @ParameterizedTest
  @MethodSource("patchUserHappyPathProvider")
  public void patchUserHappyPath(PatchUserRequest request, UserDto expectedUser) throws Exception {
    ResultActions result =
        requestUtils.performPatchRequest(GET_USER_BY_ID + "20", request, status().isOk());

    TestAssertions.assertUser(result, expectedUser);
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
        requestUtils.performPatchRequest(GET_USER_BY_ID + "20", request, expectedStatus);

    TestAssertions.assertError(
        result, expectedError.message(), expectedError.path(), expectedError.statusCode());
  }
  // endregion

//  // region GetUserFaculty tests
//  @Test
//  public void getUserFacultyReturnsCorrectFaculty() throws Exception {
//    ResultActions result =
//        requestUtils.performGetRequest(String.format(GET_USER_FACULTY, 1), status().isOk());
//
//    FacultyDto expectedFaculty = FacultyDto.builder().id(1L).name("Informatics").build();
//
//    facultyDtoAssertions.assertDto(result, expectedFaculty);
//  }
//
//  @Test
//  public void getUserFacultyReturnsStatus404WhenUserNotAssociatedWithFaculty() throws Exception {
//    ResultActions result =
//        requestUtils.performGetRequest(
//            String.format(GET_USER_FACULTY, 9999), status().isNotFound());
//
//    TestAssertions.assertError(
//        result,
//        "User with ID: 9999 does not have associated faculty",
//        "/api/users/9999/faculty",
//        404);
//  }
//
//  // endregion
}
