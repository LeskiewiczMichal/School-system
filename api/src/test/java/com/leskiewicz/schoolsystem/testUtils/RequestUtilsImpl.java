package com.leskiewicz.schoolsystem.testUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AllArgsConstructor
public class RequestUtilsImpl implements RequestUtils {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @Override
    public <T> T mapResponse(MvcResult result, Class<T> responseType) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        return mapper.readValue(responseBody, responseType);
    }

    @Override
    public <T> T mapResponse(MvcResult result, TypeReference<T> responseType) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        return mapper.readValue(responseBody, responseType);
    }

    @Override
    public MvcResult performPostRequest(String path, Object request, ResultMatcher expectedStatus) throws Exception {
        String requestBody = mapper.writeValueAsString(request);

        return mvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(expectedStatus)
                .andReturn();
    }

    @Override
    public ResultActions performGetRequest(String path, ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get(path)
                        .contentType("application/hal+json"))
                .andExpect(expectedStatus);
    }
}
