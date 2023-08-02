package com.leskiewicz.schoolsystem.file;

import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.files.FileController;
import com.leskiewicz.schoolsystem.files.FileService;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        mvc = MockMvcBuilders.standaloneSetup(fileController).build();
    }

    @Test
    public void downloadFile() throws Exception {
        // Prepare test data
        long fileId = 1L;
        String fileName = "example.txt";
        String fileType = "text/plain";
        byte[] fileData = "Hello, World!".getBytes();

        File file = new File();
        file.setId(fileId);
        file.setFileName(fileName);
        file.setFileType(fileType);
        file.setFileData(fileData);

        // Mock the fileService.getFileById() method
        given(fileService.getFileById(fileId)).willReturn(file);

        mvc.perform(get("/api/files/" + fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType(fileType)))
                .andExpect(content().bytes(fileData))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\""));
    }
}
