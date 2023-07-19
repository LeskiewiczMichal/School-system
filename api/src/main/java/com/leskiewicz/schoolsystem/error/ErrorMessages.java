package com.leskiewicz.schoolsystem.error;

public interface ErrorMessages {

    String USER_WITH_ID_NOT_FOUND = "User with provided id not found";
    String USER_WITH_EMAIL_NOT_FOUND = "User with provided email not found";
    String FACULTY_WITH_NAME_NOT_FOUND = "Faculty with provided name not found";

    String DEGREE_NOT_ON_FACULTY = "Degree with given title and field of study not found on chosen faculty";
}
