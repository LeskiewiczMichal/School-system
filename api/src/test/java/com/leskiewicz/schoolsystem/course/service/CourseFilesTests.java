package com.leskiewicz.schoolsystem.course.service;

import com.leskiewicz.schoolsystem.authentication.dto.CustomUserDetails;
import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.CourseServiceImpl;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.files.FileRepository;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseFilesTests {

  @Mock private CourseRepository courseRepository;
  @Mock private FileRepository fileRepository;

  @InjectMocks private CourseServiceImpl courseService;

  @Test
  public void testStoreFile() throws IOException {
    // Prepare test data
    Long courseId = 1L;
    MultipartFile mockFile = mock(MultipartFile.class);
    given(mockFile.getBytes()).willReturn("test file content".getBytes());
    given(mockFile.getOriginalFilename()).willReturn("testfile.txt");
    given(mockFile.getContentType()).willReturn("text/plain");

    // Mock authentication
    Mockito.mockStatic(AuthenticationUtils.class);
    User user = TestHelper.createUser(Mockito.mock(Faculty.class), Mockito.mock(Degree.class));
    given(AuthenticationUtils.getAuthenticatedUser()).willReturn(new CustomUserDetails(user));

    // Prepare the Course entity
    Course course = TestHelper.createCourse(Mockito.mock(Faculty.class), Mockito.mock(User.class));
    course.setId(courseId);
    course.setFiles(new ArrayList<>());

    // Prepare the expected File entity
    File expectedFile = new File();
    expectedFile.setFileName("testfile.txt");
    expectedFile.setFileType("text/plain");
    expectedFile.setUploadedBy(1L);
    expectedFile.setFileData("test file content".getBytes());

    // Mock the repository methods
    given(courseRepository.findById(courseId)).willReturn(Optional.of(course));
    given(fileRepository.save(any(File.class))).willReturn(expectedFile);

    // Call the method
    courseService.storeFile(mockFile, courseId);

    // Verify file was saved in the repository
    verify(fileRepository, times(1)).save(any(File.class));

    // Verify that the file was added to the course's files list
    assertEquals(1, course.getFiles().size());
    assertEquals(expectedFile, course.getFiles().get(0));
  }

  @Test
  public void storeFile_ThrowsEntityNotFoundException_WhenCourseWithGivenIdDoesntExist() {
    given(courseRepository.findById(anyLong())).willReturn(Optional.empty());

    // Call the method
    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> courseService.storeFile(mock(MultipartFile.class), 1L));
  }
}
