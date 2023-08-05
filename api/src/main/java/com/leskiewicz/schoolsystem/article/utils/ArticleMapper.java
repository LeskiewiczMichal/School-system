package com.leskiewicz.schoolsystem.article.utils;

import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;

public interface ArticleMapper {

    ArticleDto convertToDto(Article article);

    ArticleDto convertToDtoWithContent(Article article);

}
