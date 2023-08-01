package com.leskiewicz.schoolsystem.files;

import com.leskiewicz.schoolsystem.error.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;

  public File getFileById(Long fileId) {
    return fileRepository
        .findById(fileId)
        .orElseThrow(
            () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("File", fileId)));
  }
}
