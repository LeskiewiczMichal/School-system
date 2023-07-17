package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.security.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.security.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.error.ApiError;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:data.sql")
public class AuthenticationControllerTest {

    private String REGISTER_PATH = "/api/auth/register";

    @Autowired
    private MockMvc mvc;

//    Variables
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
//        Configure object mapper
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }

    @Test
    public void registrationHappyPath() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("Happy")
                .lastName("Path")
                .email("happypath@example.com")
                .facultyName("Informatics")
                .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
                .degreeField("Computer Science")
                .password("12345")
                .build();

        MvcResult result = performPostRequest(REGISTER_PATH, request, status().isOk());

        // Mapping response to readable objects
        JsonNode node = mapper.readTree(result.getResponse().getContentAsString());
        AuthenticationResponse response = mapResponse(result, AuthenticationResponse.class);

        Assertions.assertEquals(request.getEmail(), response.getUser().getEmail());
        Assertions.assertEquals(request.getFirstName(), response.getUser().getFirstName());
        Assertions.assertEquals(request.getLastName(), response.getUser().getLastName());
        Assertions.assertNotNull(response.getToken());
        Assertions.assertTrue(node.has("_links") && node.get("_links").has("self"), "Expected self link in response");
    }

    @ParameterizedTest
    @MethodSource("registerReturnsStatus400RequestProvider")
    public void registerReturnsStatus400OnBodyNotProvided(RegisterRequest request, String expectedErrorMessage) throws Exception {
        MvcResult result = performPostRequest(REGISTER_PATH, request, status().isBadRequest());

        ApiError response = mapResponse(result, ApiError.class);

        Assertions.assertEquals(expectedErrorMessage, response.message());
        Assertions.assertEquals(REGISTER_PATH, response.path());
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertNotNull(response.localDateTime());
    }

    static Stream<Arguments> registerReturnsStatus400RequestProvider() {
        // Each of requests has one different field missing
        return Stream.of(
                Arguments.of(RegisterRequest.builder()
                    .lastName("Path")
                    .email("happypath@example.com")
                    .facultyName("Informatics")
                    .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
                    .degreeField("Computer Science")
                    .password("12345")
                    .build(), "First name required"),
                Arguments.of(RegisterRequest.builder()
                    .firstName("Happy")
                    .email("happypath@example.com")
                    .facultyName("Informatics")
                    .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
                    .degreeField("Computer Science")
                    .password("12345")
                    .build(), "Last name required"),
                Arguments.of(RegisterRequest.builder()
                        .firstName("Happy")
                        .lastName("Path")
                        .facultyName("Informatics")
                        .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
                        .degreeField("Computer Science")
                        .password("12345")
                        .build(), "Email required"),
                Arguments.of(RegisterRequest.builder()
                        .firstName("Happy")
                        .lastName("Path")
                        .email("happypath@example.com")
                        .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
                        .degreeField("Computer Science")
                        .password("12345")
                        .build(), "Faculty name required"),
                Arguments.of(RegisterRequest.builder()
                        .firstName("Happy")
                        .lastName("Path")
                        .email("happypath@example.com")
                        .facultyName("Informatics")
                        .degreeField("Computer Science")
                        .password("12345")
                        .build(), "Degree title required"),
                Arguments.of(RegisterRequest.builder()
                        .firstName("Happy")
                        .lastName("Path")
                        .email("happypath@example.com")
                        .facultyName("Informatics")
                        .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
                        .password("12345")
                        .build(), "Degree field of study required"),
                Arguments.of(RegisterRequest.builder()
                        .firstName("Happy")
                        .lastName("Path")
                        .email("happypath@example.com")
                        .facultyName("Informatics")
                        .degreeTitle(DegreeTitle.BACHELOR_OF_SCIENCE)
                        .degreeField("Computer Science")
                        .build(), "Password required")
                );
    }

    private <T> T mapResponse(MvcResult result, Class<T> responseType) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        return mapper.readValue(responseBody, responseType);
    }

    private MvcResult performPostRequest(String path, Object request, ResultMatcher expectedStatus) throws Exception {
        String requestBody = mapper.writeValueAsString(request);

        return mvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(expectedStatus)
                .andReturn();
    }
}
