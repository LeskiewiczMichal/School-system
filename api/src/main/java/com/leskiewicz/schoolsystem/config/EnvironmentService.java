package com.leskiewicz.schoolsystem.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
@Getter
//@AllArgsConstructor
public class EnvironmentService {

    @Value("${upload.path.images}")
    private String uploadImagesPath;

}
