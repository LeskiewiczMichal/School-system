package com.leskiewicz.schoolsystem.testUtils.assertions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.test.web.servlet.ResultActions;

public class TestAssertions {

  public static void assertError(ResultActions matchers, String message, String path, int status)
      throws Exception {
    matchers
        .andExpect(jsonPath(String.format("$.message")).value(message))
        .andExpect(jsonPath(String.format("$.statusCode")).value(status))
        .andExpect(jsonPath(String.format("$.path")).value(path));
  }
}
