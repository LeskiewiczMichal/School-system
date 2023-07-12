package com.leskiewicz.schoolsystem.error;

public class MissingFieldException extends RuntimeException {

    public MissingFieldException(String fieldName) {
        super(fieldName + " is required");
    }
}
