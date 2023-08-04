package com.leskiewicz.schoolsystem.article;

import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapper;
import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.authentication.utils.ValidationUtils;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.files.FileRepository;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  // Repositories
  public final ArticleRepository articleRepository;
  public final FacultyRepository facultyRepository;
  public final UserRepository userRepository;

  // Services
  public final FileService fileService;

  // Mappers
    public final ArticleMapper articleMapper;

  @Override
  public ArticleDto getById(Long id) {
    return articleMapper.convertToDtoWithContent(articleRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Article", id))));
  }

  @Override
  @Transactional
  public Article createArticle(CreateArticleRequest request) throws IOException {
    // Create a new Article object from the CreateArticleRequest
    Article article =
        Article.builder()
            .title(request.getTitle())
            .preview(request.getPreview())
            .content(request.getContent())
            .category(request.getCategory())
            .build();

    // Set the Faculty if available
    if (request.getFacultyId() != null) {
      Faculty faculty =
          facultyRepository
              .findById(request.getFacultyId())
              .orElseThrow(
                  () ->
                      new EntityNotFoundException(
                          ErrorMessages.objectWithIdNotFound("Faculty", request.getFacultyId())));
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
    MultipartFile imageFile = request.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      // Get the original file name
      String originalFileName = imageFile.getOriginalFilename();

      // Check the file extension to determine if it's an image
      if (originalFileName != null && (originalFileName.endsWith(".jpg") || originalFileName.endsWith(".jpeg")
              || originalFileName.endsWith(".png") || originalFileName.endsWith(".gif"))) {
        // Store the image file
        String fileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf('.'));
        fileService.saveImage(imageFile, fileName);

        // Associate the File object with the Article
        article.setImageName(fileName);
      } else {
        // The uploaded file is not an image
        throw new IllegalArgumentException("Uploaded file is not an image");
      }
    }

    // Save the article
    ValidationUtils.validate(article);
    return articleRepository.save(article);
  }
}
