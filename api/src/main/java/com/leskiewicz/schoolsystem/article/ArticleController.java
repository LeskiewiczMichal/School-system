package com.leskiewicz.schoolsystem.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleDtoAssembler;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

  private final ArticleService articleService;
  private final ArticleDtoAssembler articleModelAssembler;

  private final PagedResourcesAssembler<ArticleDto> articlePagedResourcesAssembler;

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

    Link articles =
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getArticles(null))
            .withRel("articles");
    article.add(articles);

    return ResponseEntity.ok(article);
  }

  /**
   * Get all Articles
   *
   * @param request ${@link PageableRequest} with pagination parameters.
   * @return status 200 (OK) and in body the paged list of {@link ArticleDto} objects and page
   *     metadata. If there are no articles, an empty page is returned (without _embedded.articles
   *     field).
   */
  @GetMapping
  public ResponseEntity<RepresentationModel<ArticleDto>> getArticles(
      @ModelAttribute PageableRequest request) {
    Page<ArticleDto> articles = articleService.getAll(request.toPageable());
    articles = articles.map(articleModelAssembler::toModel);

    Link selfLink =
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getArticles(null))
            .withSelfRel();
    Link getByIdLink =
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getArticleById(null))
            .withRel("article");
    Link searchLink =
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).searchArticles(null, null))
            .withRel("search");

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(articlePagedResourcesAssembler.toModel(articles))
            .links(List.of(selfLink, getByIdLink, searchLink))
            .build());
  }

  /**
   * Search Articles
   *
   * @param facultyId (optional, param name "faculty") ID of the faculty to search by.
   * @param request ${@link PageableRequest} with pagination parameters.
   * @return status 200 (OK) and in body the paged list of {@link ArticleDto} objects and page
   *     metadata. If there are no articles, an empty page is returned (without _embedded.articles
   *     field).
   */
  @GetMapping("/search")
  public ResponseEntity<RepresentationModel<ArticleDto>> searchArticles(
      @RequestParam(value = "faculty", required = false) Long facultyId,
      @RequestParam(value = "category", required = false) ArticleCategory category,
      @ModelAttribute PageableRequest request) {
    Page<ArticleDto> articles;
    if (facultyId != null) {
      articles = articleService.getByFaculty(facultyId, request.toPageable());
    } else if (category != null) {
//      articles = articleService.getByCategory(category, request.toPageable());
    } else {
      return ResponseEntity.badRequest().build();
    }
    
    articles = articles.map(articleModelAssembler::toModel);

    Link selfLink =
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).searchArticles(null, null))
            .withSelfRel();
    Link articlesLink =
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getArticles(null))
            .withRel("articles");
    Link getByIdLink =
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getArticleById(null))
            .withRel("article");

    return ResponseEntity.ok(
        HalModelBuilder.halModelOf(articlePagedResourcesAssembler.toModel(articles))
            .links(List.of(selfLink, articlesLink, getByIdLink))
            .build());
  }

  /**
   * Create an Article
   *
   * @param request JSON string with the article data as in ${@link CreateArticleRequest}.
   * @param image optional image file for the article.
   * @return the created {@link ArticleDto} with status 201 (CREATED).
   * @throws IOException if provided the image file that cannot be read.
   * @throws IllegalArgumentException and returns status 400 if the given request cannot be mapped
   *     into ${@link CreateArticleRequest}.
   * @throws EntityNotFoundException and returns status 404 if provided faculty id that doesn't
   *     exist.
   */
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ArticleDto> createArticle(
      @RequestPart("article") String request,
      @RequestPart(value = "image", required = false) MultipartFile image)
      throws IOException {
    ArticleDto article =
        articleModelAssembler.toModel(articleService.createArticle(request, image));

    return ResponseEntity.created(article.getLink("self").get().toUri()).body(article);
  }
}
