package com.leskiewicz.schoolsystem.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leskiewicz.schoolsystem.testModels.CustomLink;
import com.leskiewicz.schoolsystem.testModels.TriConsumer;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.RequestUtilsImpl;
import com.leskiewicz.schoolsystem.testUtils.TestAssertions;
import com.leskiewicz.schoolsystem.testUtils.assertions.DtoAssertion;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class GenericControllerTest<T> {

  @Autowired
  private MockMvc mvc;

  protected RequestUtils requestUtils;

  @BeforeEach
  public void setup() {
    requestUtils = new RequestUtilsImpl(mvc,
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule()));
  }

  @ParameterizedTest
  @MethodSource("getApiCollectionResponsesProvider")
  public <T> void getApiCollectionResponses(String apiPath, List<T> dtos, List<CustomLink> links, DtoAssertion<T> dtoAssertion) throws Exception {
    ResultActions result = requestUtils.performGetRequest(apiPath, status().isOk());

    for (int i = 0; i < dtos.size(); i++) {
      T dto = dtos.get(i);
      dtoAssertion.assertDtoInCollection(result, i, dto);
    }

    for (CustomLink customLink : links) {
      TestAssertions.assertLink(result, customLink.getRel(), customLink.getHref());
    }
  }

}
