package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;

import java.io.IOException;

public interface ArticleService {

    ArticleDto getById(Long id);

    ArticleDto createArticle(CreateArticleRequest request) throws IOException;
}
