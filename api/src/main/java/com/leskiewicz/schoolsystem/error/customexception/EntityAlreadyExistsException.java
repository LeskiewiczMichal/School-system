package com.leskiewicz.schoolsystem.error.customexception;

public class EntityAlreadyExistsException extends RuntimeException {
  public EntityAlreadyExistsException(String message) {
    super(message);
  }
}
