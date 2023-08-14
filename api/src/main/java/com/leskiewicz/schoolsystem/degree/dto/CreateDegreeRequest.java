package com.leskiewicz.schoolsystem.degree.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CreateDegreeRequest {

  @NotNull(message = "Degree title required")
  private final DegreeTitle title;

  @NotNull(message = "Degree field of study required")
  private final String fieldOfStudy;

  @NotNull(message = "Faculty name required")
  private final String facultyName;

  @NotNull(message = "Degree description required")
  private final String description;

  @NotNull(message = "Degree length of study required")
  private final Double lengthOfStudy;

  @NotNull(message = "Degree tuition fee per year required")
  private final Double tuitionFeePerYear;

  @NotNull(message = "Degree languages required")
  private final List<Language> languages;
}
