package com.leskiewicz.schoolsystem.article.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.article.ArticleRepository;
import com.leskiewicz.schoolsystem.article.ArticleServiceImpl;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceGettersTests {

    // Repositories
    @Mock  private ArticleRepository articleRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private UserRepository userRepository;

    // Services
    @Mock private FileService fileService;

    // Mappers
    @Mock private ArticleMapper articleMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks private ArticleServiceImpl articleService;

    @Test
    public void getArticlesReturnsPagedArticles() {
        Faculty faculty = TestHelper.createFaculty();
        User author = TestHelper.createTeacher(faculty);
        List<Article> articles = List.of(
                TestHelper.createArticle(author, faculty),
                TestHelper.createArticle(author, faculty)
        );
        List<ArticleDto> articleDtos = List.of(
                TestHelper.createArticleDto(articles.get(0)),
                TestHelper.createArticleDto(articles.get(1))
        );

        CommonTests.serviceGetAll(
                Article.class,
                articles,
                articleDtos,
                articleRepository::findAll,
                articleMapper::convertToDto,
                articleService::getAll
        );
    }
}
