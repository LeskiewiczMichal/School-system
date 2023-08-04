package com.leskiewicz.schoolsystem.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

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
  @JsonIgnore
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

  @OneToOne
  @JoinColumn(name = "image_path")
  private String imagePath;
}
