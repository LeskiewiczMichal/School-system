package com.leskiewicz.schoolsystem.error;

public class APIResponses {

  public static String objectDeleted(String object, Long id) {
    return object + " with ID: " + id + " deleted successfully";
  }
}
