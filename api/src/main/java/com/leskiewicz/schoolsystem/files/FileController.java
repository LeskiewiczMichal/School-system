package com.leskiewicz.schoolsystem.files;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileController {

  private final FileService fileService;

  /**
   * Download file by id
   *
   * @param fileId id of file to download
   * @return file
   * @throws EntityNotFoundException if file with given id does not exist
   * @throws MethodArgumentTypeMismatchException if fileId is not a number
   */
  @GetMapping("/{fileId}")
  public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId) {
    File file = fileService.getFileById(fileId);

    // Set headers
    ByteArrayResource resource = new ByteArrayResource(file.getFileData());
    HttpHeaders headers = new HttpHeaders();
    headers.add(
        HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"");

    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.parseMediaType(file.getFileType()))
        .contentLength(file.getFileData().length)
        .body(resource);
  }
}
