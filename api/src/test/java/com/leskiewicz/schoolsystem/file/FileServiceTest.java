package com.leskiewicz.schoolsystem.file;

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

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    @Mock private FileRepository fileRepository;
    @InjectMocks private FileServiceImpl fileService;

    @Test
    public void getFileByIdReturnsFile() {
        File file = TestHelper.createFile();

        given(fileRepository.findById(file.getId())).willReturn(Optional.of(file));

        File result = fileService.getFileById(file.getId());

        verify(fileRepository).findById(file.getId());
        Assertions.assertEquals(file, result);
    }
}
