package com.leskiewicz.schoolsystem.article.dto;

import com.leskiewicz.schoolsystem.article.ArticleCategory;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateArticleRequest {

    @NotNull(message = "Article title required")
    private String title;

    @NotNull(message = "Article preview required")
    private String preview;

    @NotNull(message = "Article content required")
    private String content;

    @NotNull(message = "Article category required")
    private ArticleCategory category;

    private Long facultyId;

    private MultipartFile image;

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
