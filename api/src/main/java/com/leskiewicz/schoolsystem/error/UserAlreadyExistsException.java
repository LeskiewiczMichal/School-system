package com.leskiewicz.schoolsystem.error;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User with provided email already exists");
    }
}
