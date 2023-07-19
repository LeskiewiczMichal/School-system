package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.ApiError;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import com.leskiewicz.schoolsystem.testModels.CustomLink;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:usersTest.sql")
public class UserControllerTest {

    private final String GET_USERS_PATH = "/api/users";
    private final String GET_USER_BY_ID = "/api/users/";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    // Variables
    private ObjectMapper mapper;
    private RequestUtils requestUtils;

    @BeforeEach
    public void setUp() {
        // Configure object mapper
        mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

        requestUtils = new RequestUtilsImpl(mvc, mapper);
    }

    //region GetUsers tests
    @DisplayName("Get users API happy path with different params")
    @ParameterizedTest
    @MethodSource("getUsersHappyPathProvider")
    public void getUsersHappyPath(String queryString, List<UserDto> users, List<CustomLink> links) throws Exception {
        ResultActions result = requestUtils.performGetRequest(GET_USERS_PATH + queryString, status().isOk());

        for (int i = 0; i < users.size(); i++) {
            UserDto user = users.get(i);
            assertUserInCollection(result, i, user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getFaculty(), user.getDegree());
        }
        for (CustomLink customLink : links) {
            assertLink(result, customLink.getRel(), customLink.getHref());
        }
    }

    static Stream<Arguments> getUsersHappyPathProvider() {
        UserDto baseUser = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .faculty("Informatics")
                .degree("Computer Science")
                .build();

        Arguments noParams = Arguments.of(
                "",
                Arrays.asList(
                        baseUser.toBuilder().build(),
                        baseUser.toBuilder().id(2L).firstName("Alice").lastName("Smith").email("alicesmith@example.com").build(),
                        baseUser.toBuilder().id(3L).firstName("Bob").lastName("Johnson").email("bobjohnson@example.com").degree(null).build()
                ),
                Arrays.asList(
                        CustomLink.builder().rel("self").href("http://localhost/api/users?page=0&size=10&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("first").href("http://localhost/api/users?page=0&size=10&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("next").href("http://localhost/api/users?page=1&size=10&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("last").href("http://localhost/api/users?page=2&size=10&sort=id&direction=asc").build()
                )
        );

        Arguments pageOne = Arguments.of(
                "?page=1",
                Arrays.asList(
                        baseUser.toBuilder().id(11L).firstName("Alice").lastName("Smith").email("alicesmith@example.com").build(),
                        baseUser.toBuilder().id(12L).firstName("Alice").lastName("Smith").email("alicesmith@example.com").build()
                ),
                Arrays.asList(
                        CustomLink.builder().rel("self").href("http://localhost/api/users?page=1&size=10&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("first").href("http://localhost/api/users?page=0&size=10&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("next").href("http://localhost/api/users?page=2&size=10&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("last").href("http://localhost/api/users?page=2&size=10&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("prev").href("http://localhost/api/users?page=0&size=10&sort=id&direction=asc").build()
                )
        );

        Arguments descending = Arguments.of(
                "?direction=desc",
                Arrays.asList(
                        baseUser.toBuilder().id(25L).firstName("Alice").lastName("Smith").email("alicesmith@example.com").build(),
                        baseUser.toBuilder().id(24L).firstName("Alice").lastName("Smith").email("alicesmith@example.com").build()
                        ),
                Arrays.asList(
                        CustomLink.builder().rel("self").href("http://localhost/api/users?page=0&size=10&sort=id&direction=desc").build(),
                        CustomLink.builder().rel("first").href("http://localhost/api/users?page=0&size=10&sort=id&direction=desc").build(),
                        CustomLink.builder().rel("next").href("http://localhost/api/users?page=1&size=10&sort=id&direction=desc").build(),
                        CustomLink.builder().rel("last").href("http://localhost/api/users?page=2&size=10&sort=id&direction=desc").build()
                )
        );

        Arguments sortByName = Arguments.of(
                "?sort=firstName",
                Arrays.asList(
                        baseUser.toBuilder().id(14L).firstName("Alice").lastName("Smith").email("alicesmith@example.com").build()),
                Arrays.asList(
                        CustomLink.builder().rel("self").href("http://localhost/api/users?page=0&size=10&sort=firstName&direction=asc").build(),
                        CustomLink.builder().rel("first").href("http://localhost/api/users?page=0&size=10&sort=firstName&direction=asc").build(),
                        CustomLink.builder().rel("next").href("http://localhost/api/users?page=1&size=10&sort=firstName&direction=asc").build(),
                        CustomLink.builder().rel("last").href("http://localhost/api/users?page=2&size=10&sort=firstName&direction=asc").build()
                )
        );

        Arguments pageSize20 = Arguments.of(
                "?size=20",
                Arrays.asList(
                ),
                Arrays.asList(
                        CustomLink.builder().rel("self").href("http://localhost/api/users?page=0&size=20&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("first").href("http://localhost/api/users?page=0&size=20&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("next").href("http://localhost/api/users?page=1&size=20&sort=id&direction=asc").build(),
                        CustomLink.builder().rel("last").href("http://localhost/api/users?page=1&size=20&sort=id&direction=asc").build()
                )
        );

        return Stream.of(noParams, pageOne, descending, sortByName, pageSize20);
    }

    //endregion

    //region GetUserById Tests
    @Test
    public void getUserByIdHappyPath() throws Exception {
        ResultActions result = requestUtils.performGetRequest(GET_USER_BY_ID + "1", status().isOk());
        assertUser(result, 1L, "John", "Doe", "johndoe@example.com", "Informatics", "Computer Science");
    }

    @Test
    public void getUserByIdReturnsStatus400OnStringProvided() throws Exception {
        ResultActions result = mvc.perform(get(GET_USER_BY_ID + "asdf"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));

        assertError(result, "Wrong argument types provided", "/api/users/asdf", 400);
    }

    @Test
    public void getUserByIdReturnsStatus404OnUserNotFound() throws Exception {
        ResultActions result = mvc.perform(get(GET_USER_BY_ID + "9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"));

        assertError(result, ErrorMessages.USER_WITH_ID_NOT_FOUND, GET_USER_BY_ID + "9999", 404);
    }
    //endregion

    //region PatchUser tests
    @DisplayName("Patch user API with different params")
    @ParameterizedTest
    @MethodSource("patchUserHappyPathProvider")
    public void patchUserHappyPath(PatchUserRequest request, UserDto expectedUser) throws Exception {
        ResultActions result = requestUtils.performPatchRequest(GET_USER_BY_ID + "20", request, status().isOk());

        assertUser(result, expectedUser);
    }

    @Test
    public void patchUserReturnsStatus404OnUserNotFound() throws Exception {
        ResultActions result = requestUtils.performPatchRequest(
                GET_USER_BY_ID + "300", PatchUserRequest.builder().firstName("Ok").build(), status().isNotFound());

        assertError(result, ErrorMessages.USER_WITH_ID_NOT_FOUND, GET_USER_BY_ID + "300", 404);
    }

    @ParameterizedTest
    @MethodSource("patchUserErrorTestingProvider")
    public void patchUserErrorTesting(PatchUserRequest request, ApiError expectedError, ResultMatcher expectedStatus) throws Exception {
        ResultActions result = requestUtils.performPatchRequest(GET_USER_BY_ID + "20", request, expectedStatus);

        assertError(result, expectedError.message(), expectedError.path(), expectedError.statusCode());
    }

    static Stream<Arguments> patchUserHappyPathProvider() {
        UserDto baseUser = UserDto.builder()
                .id(20L)
                .firstName("Alice")
                .lastName("Smith")
                .email("alicesmith@example.com")
                .faculty("Informatics")
                .degree("Computer Science")
                .build();

        Arguments changeName = Arguments.of(
                PatchUserRequest.builder().firstName("Michal").build(),
                baseUser.toBuilder().firstName("Michal").build()
        );

        Arguments changeLastName = Arguments.of(
                PatchUserRequest.builder().lastName("Leskiewicz").build(),
                baseUser.toBuilder().lastName("Leskiewicz").build()
        );

        Arguments changeEmail = Arguments.of(
                PatchUserRequest.builder().email("test@example.com").build(),
                baseUser.toBuilder().email("test@example.com").build()
        );

        Arguments changeDegree = Arguments.of(
                PatchUserRequest.builder().degreeField("Software Engineering").degreeTitle(DegreeTitle.MASTER).build(),
                baseUser.toBuilder().degree("Software Engineering").build()
        );

        Arguments changeFaculty = Arguments.of(
                PatchUserRequest.builder().facultyName("Biology").build(),
                baseUser.toBuilder().faculty("Biology").build()
        );

        Arguments changeFacultyAndDegree = Arguments.of(
                PatchUserRequest.builder().facultyName("Biology").degreeTitle(DegreeTitle.BACHELOR).degreeField("Nano").build(),
                baseUser.toBuilder().faculty("Biology").degree("Nano").build()
        );

        return Stream.of(changeName, changeLastName, changeEmail, changeDegree, changeFaculty, changeFacultyAndDegree);
    }

    static Stream<Arguments> patchUserErrorTestingProvider() {
        String path = "/api/users/20";

        Arguments changeDegreeToNotCorrectOnCurrentFaculty = Arguments.of(
                PatchUserRequest.builder().degreeField("Nano").degreeTitle(DegreeTitle.BACHELOR).build(),
                new ApiError(path, ErrorMessages.DEGREE_NOT_ON_FACULTY, 404, LocalDateTime.now()),
                status().isNotFound()
        );

        Arguments changeFacultyToNonExistent = Arguments.of(
                PatchUserRequest.builder().facultyName("qwre").build(),
                new ApiError(path, ErrorMessages.FACULTY_WITH_NAME_NOT_FOUND, 404, LocalDateTime.now()),
                status().isNotFound()
        );

        Arguments changeFacultyToOneThatHaveNotGotTheSameDegree = Arguments.of(
                PatchUserRequest.builder().facultyName("Electronics").build(),
                new ApiError(path, ErrorMessages.DEGREE_NOT_ON_FACULTY, 404, LocalDateTime.now()),
                status().isNotFound()
        );

        Arguments changeFacultyAndDegreeButDegreeButDegreeIsNotOnNewFaculty = Arguments.of(
                PatchUserRequest.builder().facultyName("Electronics").degreeTitle(DegreeTitle.BACHELOR).degreeField("Nano").build(),
                new ApiError(path, ErrorMessages.DEGREE_NOT_ON_FACULTY, 404, LocalDateTime.now()),
                status().isNotFound()
        );

        return Stream.of(
                changeDegreeToNotCorrectOnCurrentFaculty,
                changeFacultyToNonExistent,
                changeFacultyToOneThatHaveNotGotTheSameDegree,
                changeFacultyAndDegreeButDegreeButDegreeIsNotOnNewFaculty);
    }
    //endregion



    //region Utils
    private void assertUserInCollection(ResultActions matchers, int index, long id, String firstName, String lastName, String email, String faculty, String degree) throws Exception {
        matchers.andExpect(jsonPath(String.format("$._embedded.users[%d].id", index)).value(id))
                .andExpect(jsonPath(String.format("$._embedded.users[%d].firstName", index)).value(firstName))
                .andExpect(jsonPath(String.format("$._embedded.users[%d].lastName", index)).value(lastName))
                .andExpect(jsonPath(String.format("$._embedded.users[%d].email", index)).value(email))
                .andExpect(jsonPath(String.format("$._embedded.users[%d].faculty", index)).value(faculty))
                .andExpect(jsonPath(String.format("$._embedded.users[%d]._links.self.href", index)).value(String.format("http://localhost/api/users/%d", id)));
                if (degree != null) {
                    matchers.andExpect(jsonPath(String.format("$._embedded.users[%d].degree", index)).value(degree));
                }
    }


    private void assertUser(ResultActions matchers, long id, String firstName, String lastName, String email, String faculty, String degree) throws Exception {
        matchers.andExpect(jsonPath(String.format("$.id")).value(id))
                .andExpect(jsonPath(String.format("$.firstName")).value(firstName))
                .andExpect(jsonPath(String.format("$.lastName")).value(lastName))
                .andExpect(jsonPath(String.format("$.email")).value(email))
                .andExpect(jsonPath(String.format("$.faculty")).value(faculty))
                .andExpect(jsonPath(String.format("$._links.self.href")).value(String.format("http://localhost/api/users/%d", id)));
        if (degree != null) {
            matchers.andExpect(jsonPath(String.format("$.degree")).value(degree));
        }
    }

    private void assertUser(ResultActions matchers, UserDto userDto) throws Exception {
        matchers.andExpect(jsonPath(String.format("$.id")).value(userDto.getId()))
                .andExpect(jsonPath(String.format("$.firstName")).value(userDto.getFirstName()))
                .andExpect(jsonPath(String.format("$.lastName")).value(userDto.getLastName()))
                .andExpect(jsonPath(String.format("$.email")).value(userDto.getEmail()))
                .andExpect(jsonPath(String.format("$.faculty")).value(userDto.getFaculty()))
                .andExpect(jsonPath(String.format("$._links.self.href")).value(String.format("http://localhost/api/users/%d", userDto.getId())));
        if (userDto.getDegree() != null) {
            matchers.andExpect(jsonPath(String.format("$.degree")).value(userDto.getDegree()));
        }
    }

    private void assertLink(ResultActions matchers, String rel, String href) throws Exception {
        matchers.andExpect(jsonPath(String.format("$._links.%s.href", rel)).value(href));
    }

    private void assertError(ResultActions matchers, String message, String path, int status) throws Exception {
        matchers.andExpect(jsonPath(String.format("$.message")).value(message))
                .andExpect(jsonPath(String.format("$.statusCode")).value(status))
                .andExpect(jsonPath(String.format("$.path")).value(path));
    }
    //endregion


}
