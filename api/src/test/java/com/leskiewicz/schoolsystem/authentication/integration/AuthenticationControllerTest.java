package com.leskiewicz.schoolsystem.authentication.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterTeacherRequest;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.ApiError;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:authentication.sql"})
public class AuthenticationControllerTest {

  private final String REGISTER_PATH = "/api/auth/register";
  private final String AUTHENTICATE_PATH = "/api/auth/authenticate";
  private final String REGISTER_TEACHER_PATH = "/api/auth/register-teacher";

  RegisterRequest registerRequest;
  @Autowired private MockMvc mvc;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserRepository userRepository;
  @Autowired private DegreeRepository degreeRepository;
  @Autowired private FacultyRepository facultyRepository;
  //    Variables
  private ObjectMapper mapper;
  private RequestUtils requestUtils;

  // region Providers
  static Stream<Arguments> registerReturnsStatus400RequestProvider() {
    // Each of requests has one different field missing
    RegisterRequest baseRequest =
        RegisterRequest.builder()
            .firstName("Happy")
            .lastName("Path")
            .email("happypath@example.com")
            .facultyName("Informatics")
            .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
            .degreeField("Computer Science")
            .password("12345")
            .build();

    return Stream.of(
        Arguments.of(baseRequest.toBuilder().firstName(null).build(), "First name required"),
        Arguments.of(baseRequest.toBuilder().lastName(null).build(), "Last name required"),
        Arguments.of(baseRequest.toBuilder().email(null).build(), "Email required"),
        Arguments.of(baseRequest.toBuilder().facultyName(null).build(), "Faculty name required"),
        Arguments.of(baseRequest.toBuilder().degreeTitle(null).build(), "Degree title required"),
        Arguments.of(
            baseRequest.toBuilder().degreeField(null).build(), "Degree field of study required"),
        Arguments.of(baseRequest.toBuilder().password(null).build(), "Password required"));
  }

  static Stream<Arguments> authenticationReturnsStatus400OnBodyNotProvidedProvider() {
    AuthenticationRequest baseRequest =
        AuthenticationRequest.builder().email("johndoe@example.com").password("12345").build();

    return Stream.of(
        Arguments.of(baseRequest.toBuilder().email(null).build(), "Email required"),
        Arguments.of(baseRequest.toBuilder().password(null).build(), "Password required"));
  }
  // endregion

  @BeforeEach
  public void setUp() {
    // Configure object mapper
    mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    registerRequest =
        RegisterRequest.builder()
            .firstName("Happy")
            .lastName("Path")
            .email("happypath@example.com")
            .facultyName("Informatics")
            .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
            .degreeField("Computer Science")
            .password("12345")
            .build();

    requestUtils = new RequestUtilsImpl(mvc, mapper);
  }

  // region Registration Tests
  @Test
  public void registrationHappyPath() throws Exception {
    ResultActions result =
        requestUtils.performPostRequest(REGISTER_PATH, registerRequest, status().isCreated());

    // Create userDto that should be provided with response
    com.leskiewicz.schoolsystem.testModels.UserDto registerTestUserDto =
        com.leskiewicz
            .schoolsystem
            .testModels
            .UserDto
            .builder()
            .id(5L)
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .email(registerRequest.getEmail())
            .faculty(registerRequest.getFacultyName())
            .degree(registerRequest.getDegreeField())
            .build();

    // Check if Location header was added
    String expectedLocation = "http://localhost/api/users/5";
    result.andExpect(MockMvcResultMatchers.header().string("Location", expectedLocation));

    // Correct user was returned
    result.andExpect(MockMvcResultMatchers.jsonPath("$.user").exists());
    result.andExpect(MockMvcResultMatchers.jsonPath("$.user").isNotEmpty());

    //     Token was returned
    result.andExpect(jsonPath("$.token").exists());
    // Proper links were added
    result.andExpect(jsonPath("$._links.self").exists());
    result.andExpect(jsonPath("$._links.authenticate").exists());
  }

  @ParameterizedTest
  @MethodSource("registerReturnsStatus400RequestProvider")
  public void registerReturnsStatus400OnBodyNotProvided(
      RegisterRequest request, String expectedErrorMessage) throws Exception {
    MvcResult result = performPostRequest(REGISTER_PATH, request, status().isBadRequest());

    ApiError response = mapResponse(result, ApiError.class);

    Assertions.assertEquals(expectedErrorMessage, response.message());
    Assertions.assertEquals(REGISTER_PATH, response.path());
    Assertions.assertEquals(400, response.statusCode());
    Assertions.assertNotNull(response.localDateTime());
  }

  @Test
  public void registerReturnsStatus400WhenUserWithGivenEmailAlreadyExists() throws Exception {
    RegisterRequest testRequest = registerRequest.toBuilder().email("johndoe@example.com").build();
    MvcResult result = performPostRequest(REGISTER_PATH, testRequest, status().isBadRequest());

    ApiError response = mapResponse(result, ApiError.class);

    Assertions.assertEquals(
        ErrorMessages.userWithEmailAlreadyExists(testRequest.getEmail()), response.message());
    Assertions.assertEquals(REGISTER_PATH, response.path());
    Assertions.assertEquals(400, response.statusCode());
    Assertions.assertNotNull(response.localDateTime());
  }
  // endregion

  /// region Authentication tests
  @Test
  public void authenticateHappyPath() throws Exception {
    // Query degree and faculty from provided sql
    Faculty faculty = facultyRepository.findByName("Informatics").orElse(null);
    Degree degree = degreeRepository.findById(1L).orElse(null);

    // Create and save user that we are going to log into
    User authenticationTestUser =
        User.builder()
            .id(99999L)
            .firstName("Happy")
            .lastName("Path")
            .email("authenticationhappypath@example.com")
            .role(Role.ROLE_STUDENT)
            .faculty(faculty)
            .degree(degree)
            .password(passwordEncoder.encode("1234"))
            .build();
    userRepository.save(authenticationTestUser);

    // Build request
    AuthenticationRequest authenticationRequest =
        AuthenticationRequest.builder()
            .email("authenticationhappypath@example.com")
            .password("1234")
            .build();

    // Call endpoint
    ResultActions result =
        requestUtils.performPostRequest(AUTHENTICATE_PATH, authenticationRequest, status().isOk());

    // Correct user was returned
    result.andExpect(MockMvcResultMatchers.jsonPath("$.user").exists());
    result.andExpect(MockMvcResultMatchers.jsonPath("$.user").isNotEmpty());

    //     Token was returned
    result.andExpect(jsonPath("$.token").exists());
    // Proper links were added
    result.andExpect(jsonPath("$._links.self").exists());
    result.andExpect(jsonPath("$._links.register").exists());
  }

  @ParameterizedTest
  @MethodSource("authenticationReturnsStatus400OnBodyNotProvidedProvider")
  public void authenticationReturnsStatus400OnBodyNotProvided(
      AuthenticationRequest request, String expectedErrorMessage) throws Exception {
    MvcResult result = performPostRequest(AUTHENTICATE_PATH, request, status().isBadRequest());

    ApiError response = mapResponse(result, ApiError.class);

    Assertions.assertEquals(expectedErrorMessage, response.message());
    Assertions.assertEquals(AUTHENTICATE_PATH, response.path());
    Assertions.assertEquals(400, response.statusCode());
    Assertions.assertNotNull(response.localDateTime());
  }

  @Test
  public void authenticationReturnsStatus401OnBadCredentialsProvided() throws Exception {
    AuthenticationRequest authenticationBadRequest =
        AuthenticationRequest.builder().email("bad@example.com").password("bAd").build();

    MvcResult result =
        performPostRequest(AUTHENTICATE_PATH, authenticationBadRequest, status().isUnauthorized());

    ApiError response = mapResponse(result, ApiError.class);

    Assertions.assertEquals("Incorrect email or password", response.message());
    Assertions.assertEquals(AUTHENTICATE_PATH, response.path());
    Assertions.assertEquals(401, response.statusCode());
    Assertions.assertNotNull(response.localDateTime());
  }
  // endregion

  // region Register Teacher
  @Test
  public void registerTeacherHappyPath() throws Exception {
    // Create a request
    RegisterTeacherRequest request =
        RegisterTeacherRequest.builder()
            .title(DegreeTitle.BACHELOR)
            .degreeField("Computer Science")
            .firstName("John")
            .lastName("Doe")
            .email("johntest@example.com")
            .password("12345")
            .faculty(1L)
            .build();

    // Call endpoint
    ResultActions result =
        requestUtils.performPostRequest(REGISTER_TEACHER_PATH, request, status().isCreated());

    // Check if Location header was added
    result.andExpect(MockMvcResultMatchers.header().exists("Location"));

    // user was returned
    result.andExpect(MockMvcResultMatchers.jsonPath("$.user").exists());
    result.andExpect(MockMvcResultMatchers.jsonPath("$.user").isNotEmpty());

    // Token was returned
    result.andExpect(jsonPath("$.token").exists());

    // Proper links were added
    result.andExpect(jsonPath("$._links.self").exists());
    result.andExpect(jsonPath("$._links.authenticate").exists());
  }

  @Test
  public void registerTeacherReturnsStatus400WhenUserWithGivenEmailAlreadyExists()
      throws Exception {
    // Create a request
    RegisterTeacherRequest request =
        RegisterTeacherRequest.builder()
            .title(DegreeTitle.BACHELOR)
            .degreeField("Computer Science")
            .firstName("John")
            .lastName("Doe")
            .email("johndoe@example.com")
            .password("12345")
            .faculty(1L)
            .build();
    MvcResult result = performPostRequest(REGISTER_TEACHER_PATH, request, status().isBadRequest());

    ApiError response = mapResponse(result, ApiError.class);

    Assertions.assertEquals(
        ErrorMessages.userWithEmailAlreadyExists(request.getEmail()), response.message());
    Assertions.assertEquals(REGISTER_TEACHER_PATH, response.path());
    Assertions.assertEquals(400, response.statusCode());
    Assertions.assertNotNull(response.localDateTime());
  }
  // endregion

  // region Utils
  private <T> T mapResponse(MvcResult result, Class<T> responseType) throws Exception {
    String responseBody = result.getResponse().getContentAsString();
    return mapper.readValue(responseBody, responseType);
  }

  private MvcResult performPostRequest(String path, Object request, ResultMatcher expectedStatus)
      throws Exception {
    String requestBody = mapper.writeValueAsString(request);

    return mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(expectedStatus)
        .andReturn();
  }
  // endregion
}
