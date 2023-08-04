package com.leskiewicz.schoolsystem.files;

import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;
  private final ResourceLoader resourceLoader;

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

  public void saveImage(MultipartFile imageFile, String fileName) {
    try {
      Path path = Paths.get("classpath:/static/images/" + fileName);
      Files.write(path, imageFile.getBytes());
    } catch (IOException e) {
      // Handle exception
      throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
    }
  }

  public Path getFile(String fileName) throws IOException {
    // Load the resource from the classpath
    org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:/static/images/" + fileName);

    // Get the file's Path
    return ResourceUtils.getFile(resource.getURL()).toPath();
  }
}
