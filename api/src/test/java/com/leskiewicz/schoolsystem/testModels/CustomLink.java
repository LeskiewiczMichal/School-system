package com.leskiewicz.schoolsystem.testModels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class CustomLink {
    private String href;
    private String rel;
}
