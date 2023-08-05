package com.leskiewicz.schoolsystem.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class EnvironmentService {

    @Value("${upload.path.images}")
    private String uploadImagesPath;

}
