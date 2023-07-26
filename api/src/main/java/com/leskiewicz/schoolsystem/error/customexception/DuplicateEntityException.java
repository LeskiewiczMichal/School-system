package com.leskiewicz.schoolsystem.error.customexception;

public class DuplicateEntityException extends RuntimeException {

  public DuplicateEntityException(String entity) {
    super(entity + " already exists");
  }
}
