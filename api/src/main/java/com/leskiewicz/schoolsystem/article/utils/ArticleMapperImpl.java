//package com.leskiewicz.schoolsystem.article.utils;
//
//import com.leskiewicz.schoolsystem.article.Article;
//import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
//import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
//import com.leskiewicz.schoolsystem.error.ErrorMessages;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ArticleMapperImpl implements ArticleMapper {
//
//
//    @Override
//    public ArticleDto convertToDto(Article article) {
//        // Perform manual validation
//        ValidationUtils.validate(article);
//        if (article.getId() == null) {
//            throw new IllegalArgumentException(
//                    ErrorMessages.objectInvalidPropertyMissing("Article", "id"));
//        }
//
//        return new ;
//    }
//}
