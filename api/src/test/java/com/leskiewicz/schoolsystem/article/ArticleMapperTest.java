package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapper;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapperImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ArticleMapperTest {

  @Mock private FileService fileService;
  @InjectMocks private ArticleMapperImpl articleMapper;

  User user;
  Faculty faculty;

  @BeforeEach
  public void setup() {
    user = Mockito.mock(User.class);
    faculty = Mockito.mock(Faculty.class);
  }

  @Test
  public void convertToDtoCorrect() {
    given(user.getFullName()).willReturn("TestUser");
    given(faculty.getName()).willReturn("TestFaculty");
    given(user.getId()).willReturn(1L);
    given(faculty.getId()).willReturn(1L);

    Article article = TestHelper.createArticle(user, faculty);

    ArticleDto expectedArticleDto =
        ArticleDto.builder()
            .id(1L)
            .title(article.getTitle())
            .preview(article.getPreview())
            .author("TestUser")
            .authorId(1L)
            .category(article.getCategory())
            .faculty("TestFaculty")
            .facultyId(1L)
            .build();

    ArticleDto result = articleMapper.convertToDto(article);

    Assertions.assertEquals(expectedArticleDto.toString(), result.toString());
  }

  @Test
  public void convertToDto_ThrowsIllegalArgumentException_OnNoId() {
    Article article = TestHelper.createArticle(user, faculty);
    article.setId(null);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> articleMapper.convertToDto(article));
  }

  @Test
  public void convertToDtoWithContentCorrect() {
    given(user.getFullName()).willReturn("TestUser");
    given(faculty.getName()).willReturn("TestFaculty");
    given(user.getId()).willReturn(1L);
    given(faculty.getId()).willReturn(1L);

    Article article = TestHelper.createArticle(user, faculty);

    ArticleDto expectedArticleDto =
        ArticleDto.builder()
            .id(1L)
            .title(article.getTitle())
            .preview(article.getPreview())
            .author("TestUser")
            .authorId(1L)
            .category(article.getCategory())
            .faculty("TestFaculty")
            .facultyId(1L)
            .content(article.getContent())
            .build();

    ArticleDto result = articleMapper.convertToDtoWithContent(article);

    Assertions.assertEquals(expectedArticleDto.toString(), result.toString());
  }
}
