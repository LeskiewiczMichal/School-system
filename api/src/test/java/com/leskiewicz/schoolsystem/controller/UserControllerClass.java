package com.leskiewicz.schoolsystem.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:usersTest.sql")
public class UserControllerClass {

    private final String GET_USERS_PATH = "/api/users";
    private final String GET_USER_BY_ID = "/api/users/";

    @Autowired
    private MockMvc mvc;

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

    //region GetUsers Tests
    @Test
    public void getUsersHappyPath() throws Exception {
        mvc.perform(get(GET_USERS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                // User 1
                .andExpect(jsonPath("$._embedded.users[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.users[0].firstName").value("John"))
                .andExpect(jsonPath("$._embedded.users[0].lastName").value("Doe"))
                .andExpect(jsonPath("$._embedded.users[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$._embedded.users[0].faculty").value("Informatics"))
                .andExpect(jsonPath("$._embedded.users[0].degree").value("Computer Science"))
                .andExpect(jsonPath("$._embedded.users[0]._links.self.href").value("http://localhost/api/users/1"))
                // User 2
                .andExpect(jsonPath("$._embedded.users[1].id").value(2L))
                .andExpect(jsonPath("$._embedded.users[1].firstName").value("Alice"))
                .andExpect(jsonPath("$._embedded.users[1].lastName").value("Smith"))
                .andExpect(jsonPath("$._embedded.users[1].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$._embedded.users[1].faculty").value("Informatics"))
                .andExpect(jsonPath("$._embedded.users[1].degree").value("Computer Science"))
                .andExpect(jsonPath("$._embedded.users[1]._links.self.href").value("http://localhost/api/users/2"))
                // User 3
                .andExpect(jsonPath("$._embedded.users[2].id").value(3L))
                .andExpect(jsonPath("$._embedded.users[2].firstName").value("Bob"))
                .andExpect(jsonPath("$._embedded.users[2].lastName").value("Johnson"))
                .andExpect(jsonPath("$._embedded.users[2].email").value("bobjohnson@example.com"))
                .andExpect(jsonPath("$._embedded.users[2].faculty").value("Informatics"))
                .andExpect(jsonPath("$._embedded.users[2]._links.self.href").value("http://localhost/api/users/3"))
                // Links
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/users?page=0&size=10&sort=id&direction=asc"))
                .andExpect(jsonPath("$._links.first.href").value("http://localhost/api/users?page=0&size=10&sort=id&direction=asc"))
                .andExpect(jsonPath("$._links.next.href").value("http://localhost/api/users?page=1&size=10&sort=id&direction=asc"))
                .andExpect(jsonPath("$._links.last.href").value("http://localhost/api/users?page=2&size=10&sort=id&direction=asc"));

    }
    //endregion

    //region GetUserById Tests
    @Test
    public void getUserByIdHappyPath() throws Exception {
        mvc.perform(get(GET_USER_BY_ID + "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                // User 1
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.faculty").value("Informatics"))
                .andExpect(jsonPath("$.degree").value("Computer Science"))
                // Links
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/users/1"));
    }
    //endregion

}
