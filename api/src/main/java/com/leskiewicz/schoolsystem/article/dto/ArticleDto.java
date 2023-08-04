package com.leskiewicz.schoolsystem.article.dto;

import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "articles")
@AllArgsConstructor
public class ArticleDto extends RepresentationModel<Article> {

  @NonNull private final Long id;
  @NonNull private final String title;
  @NonNull private final String preview;
  @NonNull private final String content;
  private final Long author;


}
