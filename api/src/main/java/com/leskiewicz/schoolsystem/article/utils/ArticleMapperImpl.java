package com.leskiewicz.schoolsystem.article.utils;

import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.user.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ArticleMapperImpl implements ArticleMapper {

  private FileService fileService;
  private final Logger logger = LoggerFactory.getLogger(ArticleMapperImpl.class);

  @Override
  public ArticleDto convertToDto(Article article) {
    // Perform manual validation
    ValidationUtils.validate(article);
    if (article.getId() == null) {
      throw new IllegalArgumentException(
          ErrorMessages.objectInvalidPropertyMissing("Article", "id"));
    }

    ArticleDto.ArticleDtoBuilder dto =
        ArticleDto.builder()
            .id(article.getId())
            .title(article.getTitle())
            .preview(article.getPreview())
            .category(article.getCategory());

    User author = article.getAuthor();
    Faculty faculty = article.getFaculty();

    if (author != null) {
      dto.author(author.getFullName());
      dto.authorId(author.getId());
    }
    if (faculty != null) {
      dto.faculty(faculty.getName());
      dto.facultyId(faculty.getId());
    }

    if (article.getImageName() != null) {
      try {
        String path = fileService.getFile(article.getImageName()).toString();
        path = path.substring(path.indexOf("/images"));
        dto.imgPath(path);
      } catch (Exception e) {
        logger.error("Error while getting image path for article with id: " + article.getId());
        dto.imgPath(null);
      }
    }

    return dto.build();
  }

  @Override
  public ArticleDto convertToDtoWithContent(Article article) {
    ArticleDto dto = convertToDto(article);
    dto.setContent(article.getContent());
    return dto;
  }
}
