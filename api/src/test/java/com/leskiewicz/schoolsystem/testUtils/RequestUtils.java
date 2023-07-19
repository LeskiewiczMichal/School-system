package com.leskiewicz.schoolsystem.testUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

public interface RequestUtils {

    <T> T mapResponse(MvcResult result, Class<T> responseType) throws Exception;
    MvcResult performPostRequest(String path, Object request, ResultMatcher expectedStatus) throws Exception;
    ResultActions performGetRequest(String path, ResultMatcher expectedStatus) throws Exception;
    <T> T mapResponse(MvcResult result, TypeReference<T> responseType) throws Exception;
}
