package com.leskiewicz.schoolsystem.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.testModels.CustomLink;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.TestAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.DtoAssertion;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

@Disabled("This is only a generic tests provider for controller tests")
@SpringBootTest
@AutoConfigureMockMvc
public class GenericControllerTest<T> {

  @Autowired
  private MockMvc mvc;

  protected RequestUtils requestUtils;

  @BeforeEach
  public void setupTestUtils() {
    // Setting up my utils class for performing requests
    requestUtils = new RequestUtilsImpl(mvc,
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule()));
  }

  @ParameterizedTest
  @MethodSource("getApiCollectionResponsesProvider")
  public <T> void getApiCollectionResponses(String apiPath, List<T> dtos, List<CustomLink> links, DtoAssertion<T> dtoAssertion) throws Exception {
    ResultActions result = requestUtils.performGetRequest(apiPath, status().isOk());

    for (int i = 0; i < dtos.size(); i++) {
      // Assert with custom interface for each type
      T dto = dtos.get(i);
      dtoAssertion.assertDtoInCollection(result, i, dto);
    }

    for (CustomLink customLink : links) {
      TestAssertions.assertLink(result, customLink.getRel(), customLink.getHref());
    }
  }

  @ParameterizedTest
  @MethodSource("getApiSingleItemResponsesProvider")
  public <T> void getApiSinlgeItemResponses(String apiPath, ResultMatcher status, String mediaType, T dto, DtoAssertion<T> dtoAssertion) throws Exception {
    ResultActions result = requestUtils.performGetRequest(apiPath, status, mediaType);

    dtoAssertion.assertDto(result, dto);
  }

  @ParameterizedTest
  @MethodSource("getApiSingleItemErrorsProvider")
  public void getApiErrors(String apiPath, ResultMatcher status, String mediaType, String errorMessage, int apiErrorCode) throws Exception {
    ResultActions result = requestUtils.performGetRequest(apiPath, status, mediaType);

    TestAssertions.assertError(result, errorMessage, apiPath, apiErrorCode);
  }


}
