package com.leskiewicz.schoolsystem.testUtils;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommonTests {


    // This test assumes that you have three pages of data
    public static void paginationLinksTest(RequestUtils requestUtils, String baseApiPath, int lastPage) throws Exception {
        String query = "http://localhost" + baseApiPath + "?page=%d&size=%d&sort=%s&direction=%s";

        // Base query
        ResultActions result = requestUtils.performGetRequest(baseApiPath, status().isOk())
                .andExpect(jsonPath("$._links.self.href").value(String.format(query, 0, 10, "id", "asc")))
                .andExpect(jsonPath("$._links.next.href").value(String.format(query, 1, 10, "id", "asc")))
                .andExpect(jsonPath("$._links.first.href").value(String.format(query, 0, 10, "id", "asc")))
                .andExpect(jsonPath("$._links.last.href").value(String.format(query, lastPage, 10, "id", "asc")));

        // Page 1
        result = requestUtils.performGetRequest(baseApiPath + "?page=1", status().isOk())
                .andExpect(jsonPath("$._links.self.href").value(String.format(query, 1, 10, "id", "asc")))
                .andExpect(jsonPath("$._links.prev.href").value(String.format(query, 0, 10, "id", "asc")))
                .andExpect(jsonPath("$._links.first.href").value(String.format(query, 0, 10, "id", "asc")))
                .andExpect(jsonPath("$._links.last.href").value(String.format(query, lastPage, 10, "id", "asc")));

        // Descending
        result = requestUtils.performGetRequest(baseApiPath + "?direction=desc", status().isOk())
                .andExpect(jsonPath("$._links.self.href").value(String.format(query, 0, 10, "id", "desc")))
                .andExpect(jsonPath("$._links.next.href").value(String.format(query, 1, 10, "id", "desc")))
                .andExpect(jsonPath("$._links.first.href").value(String.format(query, 0, 10, "id", "desc")))
                .andExpect(jsonPath("$._links.last.href").value(String.format(query, lastPage, 10, "id", "desc")));

        // Page size 20
        result = requestUtils.performGetRequest(baseApiPath + "?size=20", status().isOk())
                .andExpect(jsonPath("$._links.self.href").value(String.format(query, 0, 20, "id", "asc")))
                .andExpect(jsonPath("$._links.first.href").value(String.format(query, 0, 20, "id", "asc")));

        // Sort by id, descending and page 1
        result = requestUtils.performGetRequest(baseApiPath + "?sort=id&direction=desc&page=1", status().isOk())
                .andExpect(jsonPath("$._links.self.href").value(String.format(query, 1, 10, "id", "desc")))
                .andExpect(jsonPath("$._links.prev.href").value(String.format(query, 0, 10, "id", "desc")))
                .andExpect(jsonPath("$._links.first.href").value(String.format(query, 0, 10, "id", "desc")))
                .andExpect(jsonPath("$._links.last.href").value(String.format(query, lastPage, 10, "id", "desc")));
    }
}
