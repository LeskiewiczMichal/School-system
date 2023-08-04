package com.leskiewicz.schoolsystem.article.dto;

import com.leskiewicz.schoolsystem.article.ArticleCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@Builder
public class CreateArticleRequest {

    @NotNull(message = "Article title required")
    private final String title;

    @NotNull(message = "Article preview required")
    private final String preview;

    @NotNull(message = "Article content required")
    private final String content;

    @NotNull(message = "Article category required")
    private final ArticleCategory category;

    private final Long facultyId;

    private final MultipartFile image;
}
