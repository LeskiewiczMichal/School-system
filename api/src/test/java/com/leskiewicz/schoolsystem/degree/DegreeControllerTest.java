package com.leskiewicz.schoolsystem.degree;

import static com.leskiewicz.schoolsystem.builders.CourseBuilder.createCourseDtoListFrom;
import static com.leskiewicz.schoolsystem.builders.CourseBuilder.createCourseList;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.springframework.http.*;
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
    DegreeDto degreeDto = degreeDtoFrom(aDegree().build());

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
  public void getDegreeByIdReturnsFormattedResponse() {
    when(degreeService.getById(any(Long.class))).thenReturn(degreeDto);
    when(degreeDtoAssembler.toModel(any(DegreeDto.class))).thenReturn(degreeDto);

    ResponseEntity<DegreeDto> result = degreeController.getDegreeById(1L);

    Assertions.assertEquals(degreeDto, result.getBody());
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  public void searchDegrees() throws Exception {
    when(
            degreeService.search(
                any(String.class), any(Long.class), any(DegreeTitle.class), any(Pageable.class)))
        .thenReturn(degreeDtoPage);
    when(degreeDtoAssembler.mapPageToModel(any(Page.class))).thenReturn(degreeDtoPage);
    when(degreePagedResourcesAssembler.toModel(any(Page.class))).thenReturn(degreePagedModel);

    ResponseEntity<RepresentationModel<DegreeDto>> response = degreeController.searchDegrees(
        "Computer Science", 101L, DegreeTitle.BACHELOR_OF_SCIENCE, new PageableRequest());

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
  }

  @Test
  public void getDegreeCoursesReturnsFormattedResponse() throws Exception {
    List<CourseDto> courseDtoList = createCourseDtoListFrom(createCourseList());
    Page<CourseDto> courseDtoPage = new PageImpl<>(courseDtoList);

    when(degreeService.getDegreeCourses(any(Long.class), any(Pageable.class))).thenReturn(courseDtoPage);
    when(courseDtoAssembler.toModel(any(CourseDto.class))).thenReturn(courseDtoList.get(0), courseDtoList.get(1));
    when(coursePagedResourcesAssembler.toModel(any(Page.class))).thenReturn(PagedModel.of(courseDtoList, new PagedModel.PageMetadata(1, 1, 1, 1)));

    mvc.perform(get(DEGREES_ENDPOINT + "/1/courses").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page").exists())
            .andExpect(jsonPath("$.links").isArray())
            .andReturn();
  }



  @Test
  public void uploadImageReturnsFormattedResponse() throws Exception {
    doNothing().when(degreeService).addImage(any(Long.class), any(MultipartFile.class));

    MockMultipartFile file = createMockFile();

    mvc.perform(
            multipart(DEGREES_ENDPOINT + "/1/image")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("File with name: test.txt uploaded successfully"))
        .andExpect(jsonPath("$.links[*].rel", Matchers.hasItems("degree")))
        .andExpect(jsonPath("$.links[*].href", Matchers.hasSize(1)))
        .andReturn();
  }

  private MockMultipartFile createMockFile() {
    return new MockMultipartFile(
        "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
  }
}
