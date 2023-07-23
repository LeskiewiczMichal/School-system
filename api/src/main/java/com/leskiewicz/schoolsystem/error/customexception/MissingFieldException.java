package com.leskiewicz.schoolsystem.error.customexception;

public class MissingFieldException extends RuntimeException {

  public MissingFieldException(String message) {
    super(message);
  }
}
