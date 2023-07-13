package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.dto.request.RegisterRequest;
import com.leskiewicz.schoolsystem.dto.response.AuthenticationResponse;
import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.model.enums.DegreeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:data.sql")
public class AuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

//    Variables
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
        String requestBody = mapper.writeValueAsString(request);

        MvcResult result = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        //
        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = mapper.readTree(responseBody);
        AuthenticationResponse response = mapper.readValue(responseBody, AuthenticationResponse.class);

        Assertions.assertEquals(request.getEmail(), response.getUser().getEmail());
        Assertions.assertNotNull(response.getToken());
        Assertions.assertTrue(node.has("_links") && node.get("_links").has("self"), "Expected self link in response");
    }

}
