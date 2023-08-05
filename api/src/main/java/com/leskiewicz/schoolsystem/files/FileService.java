package com.leskiewicz.schoolsystem.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileService {

  File getFileById(Long fileId);

  File saveFile(MultipartFile file) throws IOException;

  void saveImage(MultipartFile imageFile, String fileName);

//  Path getFile(String fileName) throws IOException;

  String getImageUrlPath(String fileName);

  String uploadImage(MultipartFile image);
}
