package com.leskiewicz.schoolsystem.error;

public class MissingFieldException extends RuntimeException {

    public MissingFieldException(String message) {
        super(message);
    }
}
