package com.leskiewicz.schoolsystem.error.customexception;

public class JWTAuthenticationException extends RuntimeException {

  public JWTAuthenticationException(String message) {
    super(message);
  }
}
