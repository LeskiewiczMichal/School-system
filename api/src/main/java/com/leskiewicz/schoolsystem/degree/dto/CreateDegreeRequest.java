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

  @NotNull(message = "Degree title required") private final DegreeTitle title;
  @NotNull(message = "Degree field of study required") private final String fieldOfStudy;
  @NotNull(message = "Faculty name required") private final String facultyName;
}
