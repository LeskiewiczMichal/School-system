package com.leskiewicz.schoolsystem.article.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.article.ArticleCategory;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Getter
@Builder
@Relation(collectionRelation = "articles")
@AllArgsConstructor
public class ArticleDto extends RepresentationModel<Article> {

  @NonNull private final Long id;
  @NonNull private final String title;
  @NonNull private final String preview;
  @NonNull private final ArticleCategory category;
  private String content;
  private final String author;
  private final String faculty;
  private final String imgPath;

  @JsonIgnore private final Long authorId;
  @JsonIgnore private final Long facultyId;

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "ArticleDto{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", preview='" + preview + '\'' +
            ", category=" + category +
            ", content='" + content + '\'' +
            ", author='" + author + '\'' +
            ", faculty='" + faculty + '\'' +
            ", imgPath='" + imgPath + '\'' +
            ", authorId=" + authorId +
            ", facultyId=" + facultyId +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ArticleDto that = (ArticleDto) o;
    return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(preview, that.preview) && category == that.category;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id, title, preview, category);
  }
}
