package com.leskiewicz.schoolsystem.testUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

public interface RequestUtils {

  <T> T mapResponse(MvcResult result, Class<T> responseType) throws Exception;

  MvcResult performPostRequestResult(String path, Object request, ResultMatcher expectedStatus)
      throws Exception;

  ResultActions performPostRequest(String path, Object request, ResultMatcher expectedStatus)
      throws Exception;

  ResultActions performPatchRequest(String path, Object request, ResultMatcher expectedStatus)
      throws Exception;

  ResultActions performGetRequest(String path, ResultMatcher expectedStatus) throws Exception;

  public ResultActions performGetRequest(
      String path, ResultMatcher expectedStatus, String expectedContentType) throws Exception;

  <T> T mapResponse(MvcResult result, TypeReference<T> responseType) throws Exception;
}
