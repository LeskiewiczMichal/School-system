package com.leskiewicz.schoolsystem.files;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileController {

  private final FileService fileService;

  @GetMapping("/{fileId}")
  public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId) {
    File file = fileService.getFileById(fileId);

    ByteArrayResource resource = new ByteArrayResource(file.getFileData());
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"");


    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.parseMediaType(file.getFileType()))
        .contentLength(file.getFileData().length)
        .body(resource);
  }
}
