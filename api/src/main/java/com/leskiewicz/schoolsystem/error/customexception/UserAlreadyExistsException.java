package com.leskiewicz.schoolsystem.error.customexception;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
