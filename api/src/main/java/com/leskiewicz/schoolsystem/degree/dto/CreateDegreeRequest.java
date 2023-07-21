package com.leskiewicz.schoolsystem.degree.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateDegreeRequest {

  @NotNull private final DegreeTitle title;
  @NotNull private final String fieldOfStudy;
  @NotNull private final String facultyName;
}
