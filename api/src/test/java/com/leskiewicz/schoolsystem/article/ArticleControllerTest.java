package com.leskiewicz.schoolsystem.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleDtoAssembler;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.error.DefaultExceptionHandler;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerTest {

  // Mocks
  private ArticleService articleService;
  private ArticleDtoAssembler articleModelAssembler;
  private PagedResourcesAssembler<ArticleDto> articlePagedResourcesAssembler;
  private ArticleController articleController;

  private MockMvc mvc;

  // Variables
  private User author;
  private Faculty faculty;
  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    articleService = Mockito.mock(ArticleService.class);
    articleModelAssembler = Mockito.mock(ArticleDtoAssembler.class);
    articlePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    articleController =
        new ArticleController(
            articleService, articleModelAssembler, articlePagedResourcesAssembler);
    mvc =
        MockMvcBuilders.standaloneSetup(articleController)
            .setControllerAdvice(new DefaultExceptionHandler())
            .build();

    faculty = TestHelper.createFaculty();
    author = TestHelper.createUser(faculty, Mockito.mock(Degree.class));
  }

  @Test
  public void getArticleById() throws Exception {
    // Prepare test data
    ArticleDto articleDto = TestHelper.createArticleDto();

    // Mocks
    given(articleService.getById(1L)).willReturn(articleDto);
    given(articleModelAssembler.toModel(any(ArticleDto.class))).willReturn(articleDto);

    // Test
    mvc.perform(get("/api/articles/1").accept("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.title").value(articleDto.getTitle()))
        .andExpect(jsonPath("$.preview").value(articleDto.getPreview()))
        .andExpect(jsonPath("$.category").value(articleDto.getCategory().toString()))
        .andExpect(jsonPath("$.content").value(articleDto.getContent()))
        .andExpect(jsonPath("$.author").value(articleDto.getAuthor()))
        .andExpect(jsonPath("$.faculty").value(articleDto.getFaculty()))
        .andExpect(jsonPath("$.imgPath").value(articleDto.getImgPath()))
        .andExpect(jsonPath("$.links").exists())
        .andExpect(jsonPath("$.links[*].rel").value("articles"));
  }

  @Test
  public void getById_returnsStatus404_whenArticleDoesNotExist() throws Exception {
    given(articleService.getById(1L)).willThrow(new EntityNotFoundException("Article not found"));

    mvc.perform(get("/api/articles/1").accept("application/json"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Article not found"))
        .andExpect(jsonPath("$.statusCode").value(404))
        .andExpect(jsonPath("$.path").value("/api/articles/1"))
        .andExpect(jsonPath("$.localDateTime").exists());
  }

  @Test
  public void getById_ReturnsStatus400_WhenIdIsNotValid() throws Exception {
    mvc.perform(get("/api/articles/abc").accept("application/json"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Wrong argument types provided"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.path").value("/api/articles/abc"))
        .andExpect(jsonPath("$.localDateTime").exists());
  }

  @Test
  public void getAllArticles() {
    CommonTests.controllerGetEntities(
        ArticleDto.class,
        articlePagedResourcesAssembler,
        articleService::getAll,
        articleModelAssembler::toModel,
        articleController::getArticles);
  }

  @Test
  public void createArticle() throws Exception {
    // Prepare test data
    Faculty faculty = TestHelper.createFaculty();
    User author = TestHelper.createUser(faculty, Mockito.mock(Degree.class));
    Article article = TestHelper.createArticle(author, faculty);
    ArticleDto articleDto = TestHelper.createArticleDto(article);
    articleDto.add(
        WebMvcLinkBuilder.linkTo(ArticleController.class).slash(article.getId()).withSelfRel());
    CreateArticleRequest createArticleRequest =
        CreateArticleRequest.builder()
            .title("title")
            .content("content")
            .facultyId(1L)
            .preview("preview")
            .category(ArticleCategory.EVENTS)
            .build();
    String request = objectMapper.writeValueAsString(createArticleRequest);
    MultipartFile image = Mockito.mock(MultipartFile.class);

    // Mocks
    lenient()
        .when(articleService.createArticle(any(String.class), any(MultipartFile.class)))
        .thenReturn(articleDto);
    lenient().when(articleModelAssembler.toModel(any(ArticleDto.class))).thenReturn(articleDto);

    mvc.perform(
            multipart("/api/articles")
                .part(new MockPart("article", request.getBytes()))
                .file("image", image.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.title").value(articleDto.getTitle()))
        .andExpect(jsonPath("$.preview").value(articleDto.getPreview()))
        .andExpect(jsonPath("$.category").value(articleDto.getCategory().toString()))
        .andExpect(jsonPath("$.content").value(articleDto.getContent()))
        .andExpect(jsonPath("$.author").value(articleDto.getAuthor()))
        .andExpect(jsonPath("$.faculty").value(articleDto.getFaculty()))
        .andExpect(jsonPath("$.imgPath").value(articleDto.getImgPath()))
        .andExpect(jsonPath("$.links").exists());
  }

  @Test
  public void createArticle_returnsStatus400_whenArticlePartNotProvided() throws Exception {
    // Prepare test data
    Article article = TestHelper.createArticle(author, faculty);
    ArticleDto articleDto = TestHelper.createArticleDto(article);
    articleDto.add(
        WebMvcLinkBuilder.linkTo(ArticleController.class).slash(article.getId()).withSelfRel());
    CreateArticleRequest createArticleRequest =
        CreateArticleRequest.builder()
            .title("title")
            .content("content")
            .facultyId(1L)
            .preview("preview")
            .category(ArticleCategory.EVENTS)
            .build();
    String request = objectMapper.writeValueAsString(createArticleRequest);
    MultipartFile image = Mockito.mock(MultipartFile.class);

    mvc.perform(
            multipart("/api/articles")
                .part(new MockPart("BAD", request.getBytes()))
                .file("image", image.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
