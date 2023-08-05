package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ArticleService {

    ArticleDto getById(Long id);

    Page<ArticleDto> getAll(Pageable pageable);

    ArticleDto createArticle(String request, MultipartFile image) throws IOException;
}
