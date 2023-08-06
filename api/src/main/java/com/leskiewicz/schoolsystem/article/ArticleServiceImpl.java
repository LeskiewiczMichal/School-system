package com.leskiewicz.schoolsystem.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapper;
import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  // Repositories
  private final ArticleRepository articleRepository;
  private final FacultyRepository facultyRepository;
  private final UserRepository userRepository;

  // Services
  private final FileService fileService;

  // Mappers
  private final ArticleMapper articleMapper;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public ArticleDto getById(Long id) {
    return articleMapper.convertToDtoWithContent(
        articleRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound("Article", id))));
  }

  @Override
  public Page<ArticleDto> getByFaculty(Long facultyId, Pageable pageable) {
    if (!facultyRepository.existsById(facultyId)) {
      throw new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Faculty", facultyId));
    }

    Page<Article> articles = articleRepository.findArticlesByFacultyId(facultyId, pageable);
    return articles.map(articleMapper::convertToDto);
  }

  @Override
  public Page<ArticleDto> getByCategory(ArticleCategory category, Pageable pageable) {
    Page<Article> articles = articleRepository.findArticlesByCategory(category, pageable);
    return articles.map(articleMapper::convertToDto);
  }

  @Override
  public Page<ArticleDto> getByFacultyAndCategory(Long facultyId, ArticleCategory category, Pageable pageable) {
    return null;
  }

  @Override
  public Page<ArticleDto> getAll(Pageable pageable) {
    Page<Article> articles = articleRepository.findAll(pageable);
    return articles.map(articleMapper::convertToDto);
  }

  @Override
  @Transactional
  public ArticleDto createArticle(String request, MultipartFile image) throws IOException {
    CreateArticleRequest articleRequest;
    try {
      articleRequest = objectMapper.readValue(request, CreateArticleRequest.class);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to parse JSON into CreateArticleRequest");
    }

    // Create a new Article object from the CreateArticleRequest
    Article article =
        Article.builder()
            .title(articleRequest.getTitle())
            .preview(articleRequest.getPreview())
            .content(articleRequest.getContent())
            .category(articleRequest.getCategory())
            .build();

    // Set the Faculty if available
    if (articleRequest.getFacultyId() != null) {
      Faculty faculty =
          facultyRepository
              .findById(articleRequest.getFacultyId())
              .orElseThrow(
                  () ->
                      new EntityNotFoundException(
                          ErrorMessages.objectWithIdNotFound(
                              "Faculty", articleRequest.getFacultyId())));
      article.setFaculty(faculty);
    }

    // Get article author from authorization context
    article.setAuthor(
        userRepository
            .findById(AuthenticationUtils.getAuthenticatedUser().getId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        ErrorMessages.objectWithIdNotFound(
                            "User", AuthenticationUtils.getAuthenticatedUser().getId()))));

    // Handle the image if available
    if (image != null) {
      String filename = fileService.uploadImage(image);
      article.setImageName(filename);
    }

    // Save the article
    ValidationUtils.validate(article);
    return articleMapper.convertToDto(articleRepository.save(article));
  }
}
