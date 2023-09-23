package com.leskiewicz.schoolsystem.file;

import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.files.FileRepository;
import com.leskiewicz.schoolsystem.files.FileServiceImpl;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.leskiewicz.schoolsystem.builders.FileBuilder.aFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
  @Mock private FileRepository fileRepository;
  @Mock private AuthenticationUtils authenticationUtils;
  @InjectMocks private FileServiceImpl fileService;

  @Test
  public void getFileByIdReturnsCorrectFile() {
    File file = aFile().build();
    given(fileRepository.findById(file.getId())).willReturn(Optional.of(file));

    File result = fileService.getFileById(file.getId());

    Assertions.assertEquals(file, result);
  }

  @Test
  public void saveFileCreatesAndSavesFile() throws Exception {
    File file = aFile().build();
    MultipartFile multipartFile =
        new MockMultipartFile("testFile", "testFile.png", "image/png", new byte[] {1, 2, 3});
    when(authenticationUtils.getAuthenticatedUserId()).thenReturn(1L);
    when(fileRepository.save(any(File.class))).thenReturn(aFile().build());

    File result = fileService.saveFile(multipartFile);

    verify(fileRepository).save(any(File.class));
    Assertions.assertEquals(file.getFileName(), result.getFileName());
    Assertions.assertEquals(file.getFileType(), result.getFileType());
    Assertions.assertEquals(file.getUploadedBy(), result.getUploadedBy());
    Assertions.assertEquals(file.getFileData().length, result.getFileData().length);
  }
}
