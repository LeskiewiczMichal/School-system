package com.leskiewicz.schoolsystem.degree;

import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.createDegreeDtoListFrom;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.createDegreeList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.error.DefaultExceptionHandler;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import java.util.Arrays;
import java.util.List;

import com.leskiewicz.schoolsystem.utils.Language;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class DegreeControllerTest {
  private final static String DEGREES_ENDPOINT = "/api/degrees";

  private DegreeService degreeService;

  // Assemblers
  private DegreeDtoAssembler degreeDtoAssembler;
  private CourseDtoAssembler courseDtoAssembler;
  private PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  private PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  DegreeController degreeController;

  private MockMvc mvc;

  // Annotation mocks didn't work as expected here, pagedResourcesAssemblers were mixed up
  @BeforeEach
  public void setUp() {
    // Create mock instances
    degreeService = Mockito.mock(DegreeService.class);
    degreeDtoAssembler = Mockito.mock(DegreeDtoAssembler.class);
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    degreePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);

    // Create DegreeController instance
    degreeController =
        new DegreeController(
            degreeService,
            degreeDtoAssembler,
            courseDtoAssembler,
            degreePagedResourcesAssembler,
            coursePagedResourcesAssembler);

    mvc =
        MockMvcBuilders.standaloneSetup(degreeController)
            .setControllerAdvice(new DefaultExceptionHandler())
            .build();
  }

  List<Degree> degreeList = createDegreeList();
  List<DegreeDto> degreeDtoList = createDegreeDtoListFrom(degreeList);
  Page<Degree> degreePage = new PageImpl<>(degreeList);
    Page<DegreeDto> degreeDtoPage = new PageImpl<>(degreeDtoList);
    PagedModel<DegreeDto> degreePagedModel = PagedModel.of(degreeDtoList, new PagedModel.PageMetadata(1, 1, 1, 1));

  @Test
  public void getDegreesReturnsFormattedResponse() throws Exception {
    when(degreeService.getDegrees(new PageableRequest().toPageable())).thenReturn(degreeDtoPage);
    when(degreeDtoAssembler.mapPageToModel(any(Page.class))).thenReturn(degreeDtoPage);
    when(degreePagedResourcesAssembler.toModel(any(Page.class))).thenReturn(degreePagedModel);

    mvc.perform(get(DEGREES_ENDPOINT).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page").exists())
            .andExpect(jsonPath("$.links").isArray())
            .andReturn();
  }

  @Test
  public void getDegreeById() {
    DegreeDto degreeDto = TestHelper.createDegreeDto("Faculty");

    CommonTests.controllerGetEntityById(
        degreeDto,
        1L,
        degreeService::getById,
        degreeDtoAssembler::toModel,
        degreeController::getDegreeById);
  }

  @Test
  public void searchDegrees() throws Exception {
    // Prepare test data
    List<DegreeDto> degreeDto = Arrays.asList(TestHelper.createDegreeDto("Faculty"));
    Page<DegreeDto> degreePage = new PageImpl<>(degreeDto);
    PagedModel<DegreeDto> degreePagedModel =
        PagedModel.of(degreeDto, new PagedModel.PageMetadata(1, 1, 1, 1));

    // Mocks
    given(
            degreeService.search(
                any(String.class), any(Long.class), any(DegreeTitle.class), any(Pageable.class)))
        .willReturn(degreePage);
    given(degreeDtoAssembler.toModel(any(DegreeDto.class))).willReturn(degreeDto.get(0));
    given(degreePagedResourcesAssembler.toModel(any(Page.class))).willReturn(degreePagedModel);

    // Perform request
    mvc.perform(
            get("/api/degrees/search")
                .param("fieldOfStudy", "Computer Science")
                .param("degreeTitle", DegreeTitle.BACHELOR_OF_SCIENCE.toString())
                .param("faculty", "101")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.links").isArray())
        .andExpect(jsonPath("$.links[0].rel").value("self"))
        .andExpect(jsonPath("$.links[1].rel").value("degrees"))
        .andExpect(jsonPath("$.links[2].rel").value("degree"))
        .andReturn();

    // Verify function calls
    verify(degreeService, times(1))
        .search(any(String.class), any(Long.class), any(DegreeTitle.class), any(Pageable.class));
    verify(degreeDtoAssembler, times(1)).toModel(any(DegreeDto.class));
    verify(degreePagedResourcesAssembler, times(1)).toModel(any(Page.class));
  }

  @Test
  public void createDegree() {
    DegreeDto degreeDto = TestHelper.createDegreeDto("Faculty");

    CommonTests.controllerCreateEntity(
        degreeDto,
        DegreeDto.class,
        degreeDto.add(WebMvcLinkBuilder.linkTo(DegreeController.class).withSelfRel()),
        CreateDegreeRequest.class,
        new CreateDegreeRequest(
            degreeDto.getTitle(),
            degreeDto.getFieldOfStudy(),
            degreeDto.getFaculty(),
            "Description",
            3.0,
            15000.00,
            List.of(Language.ENGLISH)),
        degreeService::createDegree,
        degreeDtoAssembler::toModel,
        degreeController::createDegree);
  }

  @Test
  public void getDegreeCourses() {
    CommonTests.controllerGetEntities(
        CourseDto.class,
        coursePagedResourcesAssembler,
        (Pageable pageable) -> degreeService.getDegreeCourses(1L, pageable),
        courseDtoAssembler::toModel,
        (PageableRequest request) -> degreeController.getDegreeCourses(1L, request));
  }

  @Test
  public void uploadImage() throws Exception {
    // Mock service method to do nothing
    doNothing().when(degreeService).addImage(any(Long.class), any(MultipartFile.class));

    // Create file for testing
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    // Perform request
    mvc.perform(
            multipart("/api/degrees/1/image")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("File with name: test.txt uploaded successfully"))
        .andExpect(jsonPath("$.links[*].rel", Matchers.hasItems("degree")))
        .andExpect(jsonPath("$.links[*].href", Matchers.hasSize(1)))
        .andReturn();
  }
}
