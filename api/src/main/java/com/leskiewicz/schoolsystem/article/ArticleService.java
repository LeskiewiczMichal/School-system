package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;

public interface ArticleService {

    Article getById(Long id);

    Article createArticle(CreateArticleRequest request);
}
