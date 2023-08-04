package com.leskiewicz.schoolsystem.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

  File getFileById(Long fileId);

  File saveFile(MultipartFile file) throws IOException;

  void saveImage(MultipartFile imageFile, String fileName);
}
