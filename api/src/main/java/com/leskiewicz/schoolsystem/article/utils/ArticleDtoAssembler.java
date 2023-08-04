package com.leskiewicz.schoolsystem.article.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.article.ArticleController;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ArticleDtoAssembler extends RepresentationModelAssemblerSupport<ArticleDto, ArticleDto> {
  public ArticleDtoAssembler() {
    super(ArticleController.class, ArticleDto.class);
  }

  @Override
  public ArticleDto toModel(ArticleDto article) {
    Link selfLink =
        linkTo(methodOn(ArticleController.class).getArticleById(article.getId())).withSelfRel();
    article.add(selfLink);

    if (article.getFaculty() != null) {
      Link facultyLink =
          linkTo(methodOn(FacultyController.class).getFacultyById(article.getFacultyId()))
              .withRel("faculty");
      article.add(facultyLink);
    }

    return article;
  }
}
