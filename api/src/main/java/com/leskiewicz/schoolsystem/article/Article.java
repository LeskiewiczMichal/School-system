package com.leskiewicz.schoolsystem.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.files.File;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class Article {

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

  @NotNull
  @Column(name = "author")
  private String author;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private ArticleCategory category;

  @Column(name = "faculty_id")
  private Long facultyId;

  @OneToOne
  @Column(name = "image_id")
  private File image;
}
