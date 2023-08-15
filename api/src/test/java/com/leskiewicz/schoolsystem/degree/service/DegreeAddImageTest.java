package com.leskiewicz.schoolsystem.degree.service;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeServiceImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DegreeAddImageTest {

  @Mock private FileService fileService;
  @Mock private DegreeRepository degreeRepository;

  @InjectMocks private DegreeServiceImpl degreeService;

  @Test
  public void addImageToDegree() {
    // Prepare data
    MultipartFile file = Mockito.mock(MultipartFile.class);

    // Mocks
    given(fileService.uploadImage(any(MultipartFile.class))).willReturn("imagePath");
    given(degreeRepository.findById(any(Long.class)))
        .willReturn(Optional.of(TestHelper.createDegree(Mockito.mock(Faculty.class))));

    // Call the method
    degreeService.addImage(1L, file);

    // Verify
    verify(degreeRepository, Mockito.times(1)).save(any(Degree.class));
    verify(fileService, Mockito.times(1)).uploadImage(any(MultipartFile.class));
  }

  @Test
  public void addImage_throwsIllegalArgumentException_whenImageIsNull() {
    // Mocks
    given(degreeRepository.findById(any(Long.class)))
        .willReturn(Optional.of(TestHelper.createDegree(Mockito.mock(Faculty.class))));

    // Call the method
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          degreeService.addImage(1L, null);
        });
  }

  @Test
  public void addImage_throwsEntityNotFoundException_whenDegreeDoesNotExist() {
    // Mocks
    given(degreeRepository.findById(any(Long.class))).willReturn(Optional.empty());

    // Call the method
    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> {
          degreeService.addImage(1L, null);
        });
  }
}
