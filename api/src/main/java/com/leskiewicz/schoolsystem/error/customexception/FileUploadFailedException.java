package com.leskiewicz.schoolsystem.error.customexception;

public class FileUploadFailedException extends RuntimeException {

  public FileUploadFailedException(String message) {
    super(message);
  }
}
