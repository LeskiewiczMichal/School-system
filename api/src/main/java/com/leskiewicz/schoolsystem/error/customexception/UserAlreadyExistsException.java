package com.leskiewicz.schoolsystem.error.customexception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User with given email already exists");
    }
}
