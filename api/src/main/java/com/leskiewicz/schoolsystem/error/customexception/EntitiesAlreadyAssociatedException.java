package com.leskiewicz.schoolsystem.error.customexception;

public class EntitiesAlreadyAssociatedException extends RuntimeException {

  public EntitiesAlreadyAssociatedException(String message) {
    super(message);
  }
}
