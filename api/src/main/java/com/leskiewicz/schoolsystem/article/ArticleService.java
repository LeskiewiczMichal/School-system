package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;

import java.io.IOException;

public interface ArticleService {

    Article getById(Long id);

    Article createArticle(CreateArticleRequest request) throws IOException;
}
