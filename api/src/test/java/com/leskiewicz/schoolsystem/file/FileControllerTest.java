package com.leskiewicz.schoolsystem.file;

import com.leskiewicz.schoolsystem.error.DefaultExceptionHandler;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.files.FileController;
import com.leskiewicz.schoolsystem.files.FileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

  private FileService fileService;
  private FileController fileController;
  MockMvc mvc;

  @BeforeEach
  public void setup() {
    fileService = Mockito.mock(FileService.class);
    fileController = new FileController(fileService);

    mvc =
        MockMvcBuilders.standaloneSetup(fileController)
            .setControllerAdvice(new DefaultExceptionHandler())
            .build();
  }

  @Test
  public void downloadFile() throws Exception {
    // Prepare test data
    long fileId = 1L;
    String fileName = "example.txt";
    String fileType = "text/plain";
    byte[] fileData = "Hello, World!".getBytes();

    File file = File.builder()
            .id(fileId)
        .fileName(fileName)
        .fileType(fileType)
        .fileData(fileData).build();

    // Mock the fileService.getFileById() method
    given(fileService.getFileById(fileId)).willReturn(file);

    mvc.perform(get("/api/files/" + fileId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.parseMediaType(fileType)))
        .andExpect(content().bytes(fileData))
        .andExpect(
            header()
                .string(
                    HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\""));
  }

  @Test
  public void downloadFile_throwsEntityNotFound() throws Exception {
    // Prepare test data
    long fileId = 1L;

    // Mocks
    willThrow(new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("File", fileId)))
        .given(fileService)
        .getFileById(any(Long.class));

    // Perform the test
    mvc.perform(
            get("/api/files/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(ErrorMessages.objectWithIdNotFound("File", fileId)))
        .andExpect(jsonPath("$.statusCode").value(404))
        .andExpect(jsonPath("$.localDateTime").isNotEmpty())
        .andExpect(jsonPath("$.path").value("/api/files/" + fileId));
  }

  @Test
  public void downloadFile_throwsMethodArgumentTypeMismatchException() throws Exception {
    // Perform the test
    mvc.perform(
                    get("/api/files/qwer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Wrong argument types provided"))
            .andExpect(jsonPath("$.statusCode").value(400))
            .andExpect(jsonPath("$.localDateTime").isNotEmpty())
            .andExpect(jsonPath("$.path").value("/api/files/qwer"));
  }
}
