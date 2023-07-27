package com.leskiewicz.schoolsystem.error;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;

public class ErrorMessages {

  public static String objectWithIdNotFound(String object, Long userId) {
    return object + " with ID: " + userId + " not found";
  }

  public static String objectWithEmailNotFound(String object, String email) {
    return object + " with email: " + email + " not found";
  }

  public static String objectWithNameNotFound(String object, String facultyName) {
    return object + " with name: " + facultyName + " not found";
  }

  public static String objectAlreadyExists(String object) {
    return String.format("Identical %s already exists", object);
  }

  public static String objectWithPropertyAlreadyExists(
      String object, String propertyName, String propertyValue) {
    return String.format("%s with %s: %s already exists", object, propertyName, propertyValue);
  }

  public static String objectInvalidPropertyMissing(String object, String property) {
    return "Invalid " + object + " object: " + property + " missing";
  }

  public static String objectWasNotUpdated(String object) {
    return object + " was not updated";
  }

  public static String userIsNotTeacher(Long id) {
    return "User with ID: " + id + " is not a teacher";
  }

  public static String userIsNotStudent(Long id) {
    return "User with ID: " + id + " is not a student";
  }

  public static String degreeNotOnFaculty(
      String fieldOfStudy, DegreeTitle title, String facultyName) {
    String titleString = "Bachelor";
    switch (title) {
      case DOCTOR -> titleString = "Doctor";
      case MASTER -> titleString = "Master";
      case BACHELOR -> titleString = "Bachelor";
      case PROFESSOR -> titleString = "Professor";
      case BACHELOR_OF_SCIENCE -> titleString = "Bachelor of science";
    }

    return titleString
        + " in the field: "
        + fieldOfStudy
        + " on faculty: "
        + facultyName
        + " not found";
  }

  public static String userWithEmailAlreadyExists(String email) {
    return "User with email: " + email + " already exists";
  }
}
