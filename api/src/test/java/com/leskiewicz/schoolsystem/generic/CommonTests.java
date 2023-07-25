package com.leskiewicz.schoolsystem.generic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.RequestUtils;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CommonTests {

  public static void paginationLinksTest(
      RequestUtils requestUtils, String baseApiPath, int lastPage) throws Exception {
    String query = "http://localhost" + baseApiPath + "?page=%d&size=%d&sort=%s,%s";

    // Base query
    ResultActions result =
        requestUtils
            .performGetRequest(baseApiPath, status().isOk())
            .andExpect(
                jsonPath("$._links.self.href").value(String.format(query, 0, 10, "id", "asc")));
    expectPageSection(result);

    if (lastPage != 0) {
      result
          .andExpect(jsonPath("$._links.next.href").value(String.format(query, 1, 10, "id", "asc")))
          .andExpect(
              jsonPath("$._links.first.href").value(String.format(query, 0, 10, "id", "asc")))
          .andExpect(
              jsonPath("$._links.last.href")
                  .value(String.format(query, lastPage, 10, "id", "asc")));
    }

    // Page 1
    if (lastPage != 0) {
      result =
          requestUtils
              .performGetRequest(baseApiPath + "?page=1", status().isOk())
              .andExpect(
                  jsonPath("$._links.self.href").value(String.format(query, 1, 10, "id", "asc")))
              .andExpect(
                  jsonPath("$._links.prev.href").value(String.format(query, 0, 10, "id", "asc")))
              .andExpect(
                  jsonPath("$._links.first.href").value(String.format(query, 0, 10, "id", "asc")))
              .andExpect(
                  jsonPath("$._links.last.href")
                      .value(String.format(query, lastPage, 10, "id", "asc")));
      expectPageSection(result);
    }

    // Descending
    result =
        requestUtils
            .performGetRequest(baseApiPath + "?sort=id,desc", status().isOk())
            .andExpect(
                jsonPath("$._links.self.href").value(String.format(query, 0, 10, "id", "desc")));
    expectPageSection(result);
    if (lastPage != 0) {
      result
          .andExpect(
              jsonPath("$._links.next.href").value(String.format(query, 1, 10, "id", "desc")))
          .andExpect(
              jsonPath("$._links.first.href").value(String.format(query, 0, 10, "id", "desc")))
          .andExpect(
              jsonPath("$._links.last.href")
                  .value(String.format(query, lastPage, 10, "id", "desc")));
    }

    // Page size 20
    result =
        requestUtils
            .performGetRequest(baseApiPath + "?size=20", status().isOk())
            .andExpect(
                jsonPath("$._links.self.href").value(String.format(query, 0, 20, "id", "asc")));
    expectPageSection(result);

    // Sort by id, descending and page 1
    if (lastPage != 0) {
      result =
          requestUtils
              .performGetRequest(baseApiPath + "?sort=id,desc&page=1", status().isOk())
              .andExpect(
                  jsonPath("$._links.self.href").value(String.format(query, 1, 10, "id", "desc")))
              .andExpect(
                  jsonPath("$._links.prev.href").value(String.format(query, 0, 10, "id", "desc")))
              .andExpect(
                  jsonPath("$._links.first.href").value(String.format(query, 0, 10, "id", "desc")))
              .andExpect(
                  jsonPath("$._links.last.href")
                      .value(String.format(query, lastPage, 10, "id", "desc")));
      expectPageSection(result);
    }
  }

  private static void expectPageSection(ResultActions result) throws Exception {
    result
        .andExpect(jsonPath("$.page.size").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists())
        .andExpect(jsonPath("$.page.number").exists());
  }

  public static <T extends RepresentationModel<T>> void controllerGetEntities(
      Class<T> entityClass,
      PagedResourcesAssembler<T> pagedResourcesAssembler,
      Function<Pageable, Page<T>> getEntitiesFunction,
      Function<T, T> toModelFunction,
      Function<PageableRequest, ResponseEntity<RepresentationModel<T>>> controllerGetFunction) {
    PageableRequest request = new PageableRequest();

    // Mock the list of resourceDto
    List<T> resourceDtoList = Arrays.asList(Mockito.mock(entityClass), Mockito.mock(entityClass));

    // Mock the page
    Page<T> entityDtoPage = new PageImpl<>(resourceDtoList);

    // Mock service
    given(getEntitiesFunction.apply(request.toPageable())).willReturn(entityDtoPage);

    // Mock assembler
    given(toModelFunction.apply(any(entityClass)))
        .willReturn(resourceDtoList.get(0), resourceDtoList.get(1));

    // Mock paged resources assembler
    PagedModel<EntityModel<T>> pagedModel = Mockito.mock(PagedModel.class);
    given(pagedResourcesAssembler.toModel(any(Page.class))).willReturn(pagedModel);

    // Call controller
    ResponseEntity<RepresentationModel<T>> response = controllerGetFunction.apply(request);

    // Assert the response
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(HalModelBuilder.halModelOf(pagedModel).build(), response.getBody());

    // Verify mocks
    verify(pagedResourcesAssembler, times(1)).toModel(entityDtoPage);
  }

  /**
   * Test the controller get by id method
   *
   * @param entityDto Entity dto for testing
   * @param entityId id of the entity
   * @param getEntityByIdFunction Get by id function of the service
   * @param toModelFunction To model function of the assembler of dto type
   * @param controllerGetFunction Get by id function of the controller
   * @param <T> Entity dto type
   */
  public static <T> void controllerGetEntityById(
      T entityDto,
      Long entityId,
      Function<Long, T> getEntityByIdFunction,
      Function<T, T> toModelFunction,
      Function<Long, ResponseEntity<T>> controllerGetFunction) {

    // Mock service
    given(getEntityByIdFunction.apply(entityId)).willReturn(entityDto);

    // Mock assembler
    given(toModelFunction.apply(entityDto)).willReturn(entityDto);

    // Call controller
    ResponseEntity<T> result = controllerGetFunction.apply(entityId);

    // Verify result
    Assertions.assertEquals(entityDto, result.getBody());
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  /**
   * Test the controller delete method
   *
   * @param entityClass Class of entity dto
   * @param requestClass Class of patch request
   * @param updateEntityFunction Update function of the service
   * @param toModelFunction To model function of the assembler of dto type
   * @param controllerPatchFunction Patch function of the controller
   * @param <T> Entity dto type
   * @param <R> Patch request type
   */
  @Test
  public static <T, R> void controllerPatchEntity(
      Class<T> entityClass,
      Class<R> requestClass,
      BiFunction<R, Long, T> updateEntityFunction,
      Function<T, T> toModelFunction,
      BiFunction<R, Long, ResponseEntity<T>> controllerPatchFunction) {
    // Prepare data
    Long entityId = 1L;
    R request = Mockito.mock(requestClass);
    T existingDto = Mockito.mock(entityClass);

    // Mock service
    given(updateEntityFunction.apply(request, entityId)).willReturn(existingDto);

    // Mock assembler
    given(toModelFunction.apply(any(entityClass))).willReturn(existingDto);

    // Call controller
    ResponseEntity<T> response = controllerPatchFunction.apply(request, entityId);

    // Verify response
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(existingDto, response.getBody());
  }

  /**
   * Test the controller create method
   *
   * @param entityDto Entity dto for testing
   * @param entityClass Class of entity dto
   * @param mockedDtoWithLinks Mocked entity dto with self link added
   * @param createRequestClass Class of create request
   * @param createRequest Create request for entity
   * @param createEntityFunction Create function of the service
   * @param toModelFunction To model function of the assembler of dto type
   * @param controllerCreateFunction Create function of the controller
   * @param <T> Entity dto type
   * @param <R> Create request type
   */
  @Test
  public static <T, R> void controllerCreateEntity(
      T entityDto,
      Class<T> entityClass,
      T mockedDtoWithLinks,
      Class<R> createRequestClass,
      R createRequest,
      Function<R, T> createEntityFunction,
      Function<T, T> toModelFunction,
      Function<R, ResponseEntity<T>> controllerCreateFunction) {

    // Mock the service behavior
    given(createEntityFunction.apply(any(createRequestClass))).willReturn(entityDto);

    // Mock the assembler behavior
    given(toModelFunction.apply(any(entityClass))).willReturn(mockedDtoWithLinks);

    // Call the controller method
    ResponseEntity<T> responseEntity = controllerCreateFunction.apply(createRequest);

    // Assert the response
    Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }
}
