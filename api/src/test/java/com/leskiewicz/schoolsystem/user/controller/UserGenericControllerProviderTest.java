package com.leskiewicz.schoolsystem.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.generic.GenericControllerTest;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import com.leskiewicz.schoolsystem.testUtils.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.assertions.UserDtoAssertions;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:usersTest.sql"})
public class UserGenericControllerProviderTest extends GenericControllerTest<UserDto> {

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

  @Test
  public void getUsersTestPagination() throws Exception {
    CommonTests.paginationLinksTest(requestUtils, "/api/users", 2);
  }

  static Stream<Arguments> getApiSingleItemResponsesProvider() {
    Arguments happyPath =
        Arguments.of(
            "/api/users/1",
            status().isOk(),
            "application/hal+json",
                UserDto.builder().id(1L).firstName("John").lastName("Doe").email("johndoe@example.com").faculty("Informatics").degree("Computer Science").build(),
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
}
