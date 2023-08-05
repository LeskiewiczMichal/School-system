package com.leskiewicz.schoolsystem.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
  @GetMapping("/{id}")
  public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
    ArticleDto article = articleModelAssembler.toModel(articleService.getById(id));

    return ResponseEntity.ok(article);
  }

  @GetMapping
  public ResponseEntity<RepresentationModel<ArticleDto>> getArticles(PageableRequest request) {
    Page<ArticleDto> articles = articleService.getAll(request.toPageable());
    articles = articles.map(articleModelAssembler::toModel);

    return ResponseEntity.ok(HalModelBuilder.halModelOf(articles).build());
  }

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ArticleDto> createArticle(
      @RequestPart("article") String request,
      @RequestPart(value = "image", required = false) MultipartFile image)
      throws IOException {
    ArticleDto article =
        articleModelAssembler.toModel(articleService.createArticle(request, image));

    return ResponseEntity.ok(article);
  }

  //  @PostMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  //  public ResponseEntity<ArticleDto> createArticle(
  //          @Valid @Req("article") String request)
  //          throws IOException {
  //    CreateArticleRequest createArticleRequest;
  //    System.out.println("here");
  //    try {
  //      ObjectMapper objectMapper = new ObjectMapper();
  //      createArticleRequest = objectMapper.readValue(request, CreateArticleRequest.class);
  //    } catch (IOException e) {
  //      throw new IllegalArgumentException("Failed to parse JSON into CreateArticleRequest");
  //    }
  //
  //    ArticleDto article =
  //            articleModelAssembler.toModel(articleService.createArticle(createArticleRequest));
  //
  //    return ResponseEntity.ok(article);
  //  }
}
