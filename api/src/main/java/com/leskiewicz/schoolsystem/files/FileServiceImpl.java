package com.leskiewicz.schoolsystem.files;

import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

  public File saveFile(MultipartFile file) throws IOException {
    File fileToSave = new File();
    fileToSave.setFileName(file.getOriginalFilename());
    fileToSave.setFileType(file.getContentType());
    fileToSave.setUploadedBy(
            AuthenticationUtils.getAuthenticatedUser()
                    .getId()); // Set the ID of the user who uploaded the file.
    fileToSave.setFileData(file.getBytes());
    return fileRepository.save(fileToSave);
  }
}
