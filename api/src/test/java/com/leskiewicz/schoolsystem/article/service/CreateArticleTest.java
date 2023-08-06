package com.leskiewicz.schoolsystem.article.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leskiewicz.schoolsystem.article.ArticleRepository;
import com.leskiewicz.schoolsystem.article.ArticleServiceImpl;
import com.leskiewicz.schoolsystem.article.utils.ArticleMapper;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.user.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateArticleTest {

    // Repositories
    @Mock
    private ArticleRepository articleRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private UserRepository userRepository;

    // Services
    @Mock private FileService fileService;

    // Mappers
    @Mock private ArticleMapper articleMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private ArticleServiceImpl articleService;


}
