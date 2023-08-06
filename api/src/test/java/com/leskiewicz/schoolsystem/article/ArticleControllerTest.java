package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.utils.ArticleDtoAssembler;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.error.DefaultExceptionHandler;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
        .andExpect(jsonPath("$.links").exists());
  }

  @Test
  public void getAllArticles() {
    CommonTests.controllerGetEntities(
            ArticleDto.class,
            articlePagedResourcesAssembler,
            articleService::getAll,
            articleModelAssembler::toModel,
            articleController::getArticles
    );
  }




}
