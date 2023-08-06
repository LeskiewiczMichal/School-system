package com.leskiewicz.schoolsystem.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query(value = "SELECT a FROM Article a WHERE a.faculty.id = :facultyId")
  Page<Article> findArticlesByFacultyId(Long facultyId, Pageable pageable);

  @Query(value = "SELECT a FROM Article a WHERE a.category = :category")
  Page<Article> findArticlesByCategory(ArticleCategory category, Pageable pageable);
}
