package com.leskiewicz.schoolsystem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SupportLogger implements Support {
    private final Logger logger = LoggerFactory.getLogger(SupportLogger.class);


    @Override
    public void notifyUpdated(String objectName, Long objectId) {
        logger.info("Updated " + objectName + " with id: " + objectId);
    }

    @Override
    public void notifyCreated(String objectName, Long objectId) {
        logger.info("Created " + objectName + " with id: " + objectId);
    }
}
