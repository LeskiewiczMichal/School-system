package com.leskiewicz.schoolsystem.files;

import com.leskiewicz.schoolsystem.authentication.utils.AuthenticationUtils;
import com.leskiewicz.schoolsystem.config.EnvironmentService;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;
  private final ResourceLoader resourceLoader;
  private final EnvironmentService environmentService;

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
      java.nio.file.Path path = Paths.get(environmentService.getUploadImagesPath());

      // Create the folder if it doesn't exist
      if (!Files.exists(path)) {
        Files.createDirectories(path);
      }

      // Save the image to the destination folder
      java.nio.file.Path destination = path.resolve(fileName);
      imageFile.transferTo(destination.toFile());
    } catch (IOException e) {
      // Handle exception
      throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
    }
  }

//  public Path getFile(String fileName) throws IOException {
//    // Load the resource from the classpath
//    org.springframework.core.io.Resource resource = resourceLoader.getResource(environmentService.getUploadImagesPath() + fileName);
//    System.out.println(Paths.get(resource.getURI()));
//
//
//    // Get the file's Path
//    return ResourceUtils.getFile(resource.getURL()).toPath();
//  }

  public String getImageUrlPath(String fileName) {
    return "/images/" + fileName;
  }
}
