package com.leskiewicz.schoolsystem.article.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.Article;
import com.leskiewicz.schoolsystem.article.ArticleCategory;
import com.leskiewicz.schoolsystem.article.ArticleRepository;
import com.leskiewicz.schoolsystem.article.ArticleServiceImpl;
import com.leskiewicz.schoolsystem.article.dto.ArticleDto;
import com.leskiewicz.schoolsystem.article.dto.CreateArticleRequest;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapper;
import com.leskiewicz.schoolsystem.authentication.dto.CustomUserDetails;
import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateArticleTest {

  // Repositories
  @Mock private ArticleRepository articleRepository;
  @Mock private FacultyRepository facultyRepository;
  @Mock private UserRepository userRepository;

  // Services
  @Mock private FileService fileService;

  // Mappers
  @Mock private ArticleMapper articleMapper;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @InjectMocks private ArticleServiceImpl articleService;

  // Variables
  private Faculty faculty;
  private User user;

  @BeforeEach
  public void setup() {
    Mockito.mockStatic(AuthenticationUtils.class);
    faculty = TestHelper.createFaculty();
    user = TestHelper.createTeacher(faculty);
  }

  @Test
  public void createsAndReturnsArticle() throws Exception {
    // Prepare data
    CreateArticleRequest createArticleRequest =
        CreateArticleRequest.builder()
            .title("title")
            .content("content")
            .facultyId(1L)
            .preview("preview")
            .category(ArticleCategory.EVENTS)
            .build();
    String request = objectMapper.writeValueAsString(createArticleRequest);
    MultipartFile file = Mockito.mock(MultipartFile.class);
    ArticleDto testDto = TestHelper.createArticleDto();

    // Mocks
    given(facultyRepository.findById(anyLong())).willReturn(Optional.of(faculty));
    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
    given(fileService.uploadImage(any(MultipartFile.class))).willReturn("imagePath");
    lenient().when(articleMapper.convertToDto(any(Article.class))).thenReturn(testDto);
    given(articleRepository.save(any(Article.class))).willReturn(Mockito.mock(Article.class));
    given(AuthenticationUtils.getAuthenticatedUser()).willReturn(new CustomUserDetails(user));

    // Call method
    ArticleDto newArticle = articleService.createArticle(request, file);

    // Assertions
    Assertions.assertEquals(testDto, newArticle);
    verify(articleRepository).save(any(Article.class));
    verify(fileService).uploadImage(any(MultipartFile.class));
    verify(articleMapper).convertToDto(any(Article.class));
    verify(userRepository).findById(anyLong());
    verify(facultyRepository).findById(anyLong());
  }

  @Test
  public void throwsExceptionWhenFacultyNotFound() throws Exception {
    // Prepare data
    CreateArticleRequest createArticleRequest =
        CreateArticleRequest.builder()
            .title("title")
            .content("content")
            .facultyId(1L)
            .preview("preview")
            .category(ArticleCategory.EVENTS)
            .build();
    String request = objectMapper.writeValueAsString(createArticleRequest);

    // Mocks
    given(facultyRepository.findById(anyLong())).willReturn(Optional.empty());

    // Call method
    Assertions.assertThrows(
        EntityNotFoundException.class, () -> articleService.createArticle(request, null));

    // Assertions
    verify(articleRepository, Mockito.never()).save(any(Article.class));
    verify(fileService, Mockito.never()).uploadImage(any(MultipartFile.class));
    verify(articleMapper, Mockito.never()).convertToDto(any(Article.class));
    verify(userRepository, Mockito.never()).findById(anyLong());
    verify(facultyRepository).findById(anyLong());
  }

  @Test
  public void throwsExceptionWhenUserNotAuthenticated() throws Exception {
    // Prepare data
    CreateArticleRequest createArticleRequest =
        CreateArticleRequest.builder()
            .title("title")
            .content("content")
            .facultyId(1L)
            .preview("preview")
            .category(ArticleCategory.EVENTS)
            .build();
    String request = objectMapper.writeValueAsString(createArticleRequest);

    // Mocks
    given(facultyRepository.findById(anyLong()))
        .willReturn(Optional.of(Mockito.mock(Faculty.class)));
    given(AuthenticationUtils.getAuthenticatedUser()).willReturn(null);

    // Call method
    Assertions.assertThrows(
        Exception.class, () -> articleService.createArticle(request, null));

    // Assertions
    verify(articleRepository, Mockito.never()).save(any(Article.class));
    verify(fileService, Mockito.never()).uploadImage(any(MultipartFile.class));
    verify(articleMapper, Mockito.never()).convertToDto(any(Article.class));
    verify(userRepository, Mockito.never()).findById(anyLong());
    verify(facultyRepository).findById(anyLong());
  }

  @Test
  public void throwsExceptionWhenAuthorNotFound() throws Exception {
    // Prepare data
    CreateArticleRequest createArticleRequest =
        CreateArticleRequest.builder()
            .title("title")
            .content("content")
            .facultyId(1L)
            .preview("preview")
            .category(ArticleCategory.EVENTS)
            .build();
    String request = objectMapper.writeValueAsString(createArticleRequest);

    // Mocks
    given(facultyRepository.findById(anyLong()))
        .willReturn(Optional.of(Mockito.mock(Faculty.class)));
    given(AuthenticationUtils.getAuthenticatedUser()).willReturn(new CustomUserDetails(user));
    given(userRepository.findById(anyLong())).willReturn(Optional.empty());

    // Call method
    Assertions.assertThrows(
        EntityNotFoundException.class, () -> articleService.createArticle(request, null));

    // Assertions
    verify(fileService, Mockito.never()).uploadImage(any(MultipartFile.class));
    verify(articleMapper, Mockito.never()).convertToDto(any(Article.class));
    verify(facultyRepository).findById(anyLong());
  }
}
