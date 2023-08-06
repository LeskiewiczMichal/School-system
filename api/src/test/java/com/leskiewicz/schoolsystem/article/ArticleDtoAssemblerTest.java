package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.utils.ArticleDtoAssembler;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ArticleDtoAssemblerTest {

  @InjectMocks private ArticleDtoAssembler articleDtoAssembler;

  @Test
  public void testToModelAddsCorrectLinks() {
    ArticleDto article = TestHelper.createArticleDto();

    ArticleDto result = articleDtoAssembler.toModel(article);

    Assertions.assertNotNull(result.getLink("self").get());
    Assertions.assertNotNull(result.getLink("faculty").get());
    Assertions.assertNotNull(result.getLink("author").get());

  }
}
