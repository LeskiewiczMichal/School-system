package com.leskiewicz.schoolsystem.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleDtoAssembler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ArticleDto> createArticle(
      @Valid @RequestPart("article") String request,
      @RequestPart(value = "image", required = false) MultipartFile image)
      throws IOException {
    CreateArticleRequest createArticleRequest;
    System.out.println("here");
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      createArticleRequest = objectMapper.readValue(request, CreateArticleRequest.class);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to parse JSON into CreateArticleRequest");
    }
    createArticleRequest.setImage(image);

    ArticleDto article =
        articleModelAssembler.toModel(articleService.createArticle(createArticleRequest));

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
