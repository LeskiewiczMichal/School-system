package com.leskiewicz.schoolsystem.utils;

public interface Support {

    void notifyUpdated(String objectName, Long objectId);
    void notifyCreated(String objectName, Long objectId);
}
