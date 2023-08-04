package com.leskiewicz.schoolsystem.article;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    public final ArticleService articleService;

    @GetMapping
    public Article getArticleById(Long id) {

    }
}
