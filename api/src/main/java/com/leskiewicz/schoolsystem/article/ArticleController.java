package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

  public final ArticleService articleService;
  public final ArticleDtoAssembler articleModelAssembler;

  /**
   * Get an Article by its ID
   *
   * @param id of the Article to get.
   * @return the ${@link Article} with the given ID and status 200.
   * @throws EntityNotFoundException if the Article with the given ID does not exist.
   * @throws IllegalArgumentException if the given ID is null.
   */
  @GetMapping
  public ArticleDto getArticleById(Long id) {
    return articleModelAssembler.toModel(articleService.getById(id));
  }

//  @PostMapping
//  public Article createArticle(@Valid @RequestBody CreateArticleRequest request) throws IOException {
//    return articleModelAssembler.toModel(articleService.createArticle(request));
//  }
}
