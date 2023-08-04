package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  public final ArticleRepository articleRepository;

  @Override
  public Article getById(Long id) {
    return articleRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Article", id)));
  }

  @Override
  public Article createArticle(CreateArticleRequest request) {
    return null;
  }
}
