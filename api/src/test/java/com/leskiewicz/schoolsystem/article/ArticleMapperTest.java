package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapper;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapperImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ArticleMapperTest {

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
            .content(article.getContent())
            .author("TestUser")
            .category(article.getCategory())
            .faculty("TestFaculty")
            .imgPath("/images/" + article.getImageName())
            .build();

    ArticleDto result = articleMapper.convertToDto(article);

    Assertions.assertEquals(expectedArticleDto, result);
  }

  @Test
  public void convertToDtoThrowsIllegalArgumentExceptionOnNoId() {
    Article article = TestHelper.createArticle(user, faculty);
    article.setId(null);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> articleMapper.convertToDto(article));
  }
}
