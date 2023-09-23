package com.leskiewicz.schoolsystem.builders;

import com.leskiewicz.schoolsystem.files.File;

public class FileBuilder {
  private Long id = 1L;
  private String fileName = "testFile.png";
  private String fileType = "testType";
  private Long uploadedBy = 1L;
  private byte[] fileData = new byte[] {1, 2, 3};

  public static FileBuilder aFile() {
    return new FileBuilder();
  }

  public FileBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public FileBuilder fileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  public FileBuilder fileType(String fileType) {
    this.fileType = fileType;
    return this;
  }

  public FileBuilder uploadedBy(Long uploadedBy) {
    this.uploadedBy = uploadedBy;
    return this;
  }

  public FileBuilder fileData(byte[] fileData) {
    this.fileData = fileData;
    return this;
  }

  public File build() {
    return new File(id, fileName, fileType, uploadedBy, fileData);
  }
}
