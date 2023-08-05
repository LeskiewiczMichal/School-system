package com.leskiewicz.schoolsystem.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class Article extends RepresentationModel<Article> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "title")
  private String title;

  @NotNull
  @Column(name = "preview")
  private String preview;

  @NotNull
  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "content")
  private String content;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private ArticleCategory category;

  @ManyToOne
  @JoinColumn(name = "faculty_id")
  private Faculty faculty;

  @Column(name = "image_name")
  private String imageName;

  @Override
  public String toString() {
    return "Article{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", preview='" + preview + '\'' +
            ", author=" + author +
            ", category=" + category +
            ", faculty=" + faculty +
            ", imageName='" + imageName + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Article article)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(getId(), article.getId()) && Objects.equals(getTitle(), article.getTitle()) && Objects.equals(getPreview(), article.getPreview()) && getCategory() == article.getCategory();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getId(), getTitle(), getPreview(), getCategory());
  }
}
