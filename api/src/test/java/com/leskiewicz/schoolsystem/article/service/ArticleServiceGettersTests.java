package com.leskiewicz.schoolsystem.article.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.article.ArticleRepository;
import com.leskiewicz.schoolsystem.article.ArticleServiceImpl;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapper;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceGettersTests {

  // Repositories
  @Mock private ArticleRepository articleRepository;
  @Mock private FacultyRepository facultyRepository;
  @Mock private UserRepository userRepository;

  // Services
  @Mock private FileService fileService;

  // Mappers
  @Mock private ArticleMapper articleMapper;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @InjectMocks private ArticleServiceImpl articleService;

  @Test
  public void geAllReturnsPagedArticles() {
    Faculty faculty = TestHelper.createFaculty();
    User author = TestHelper.createTeacher(faculty);
    List<Article> articles =
        List.of(
            TestHelper.createArticle(author, faculty), TestHelper.createArticle(author, faculty));
    List<ArticleDto> articleDtos =
        List.of(
            TestHelper.createArticleDto(articles.get(0)),
            TestHelper.createArticleDto(articles.get(1)));

    CommonTests.serviceGetAll(
        Article.class,
        articles,
        articleDtos,
        articleRepository::findAll,
        articleMapper::convertToDto,
        articleService::getAll);
  }

  @Test
  public void getById_throwsEntityNotFoundException_whenArticleDoesNotExist() {
    given(articleRepository.findById(anyLong())).willReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> articleService.getById(1L),
        ErrorMessages.objectWithIdNotFound("Article", 1L));
  }

  @Test
  public void getByIdReturnsArticle() {
    Faculty faculty = TestHelper.createFaculty();
    User author = TestHelper.createTeacher(faculty);
    Article article = TestHelper.createArticle(author, faculty);
    ArticleDto articleDto = TestHelper.createArticleDto(article);

    CommonTests.serviceGetById(
        Article.class,
        article,
        articleDto,
        articleRepository::findById,
        articleMapper::convertToDtoWithContent,
        articleService::getById);
  }
}
