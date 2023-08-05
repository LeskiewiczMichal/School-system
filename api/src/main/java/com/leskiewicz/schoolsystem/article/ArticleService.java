package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ArticleService {

    ArticleDto getById(Long id);

    ArticleDto createArticle(String request, MultipartFile image) throws IOException;
}
