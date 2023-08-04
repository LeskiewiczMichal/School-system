package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleModelAssembler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

  public final ArticleService articleService;
  public final ArticleModelAssembler articleModelAssembler;

  @GetMapping
  public Article getArticleById(Long id) {
    return articleModelAssembler.toModel(articleService.getById(id));
  }

  @PostMapping
  public Article createArticle(CreateArticleRequest request) throws IOException {
    return articleModelAssembler.toModel(articleService.createArticle(request));
  }
}
