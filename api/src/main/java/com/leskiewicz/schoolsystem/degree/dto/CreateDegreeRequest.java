package com.leskiewicz.schoolsystem.degree.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public record CreateDegreeRequest (

  @NotNull(message = "Degree title required")
  DegreeTitle title,

  @NotNull(message = "Degree field of study required")
  String fieldOfStudy,

  @NotNull(message = "Faculty name required")
   String facultyName,

  @NotNull(message = "Degree description required")
  String description,

  @NotNull(message = "Degree length of study required")
  Double lengthOfStudy,

  @NotNull(message = "Degree tuition fee per year required")
  Double tuitionFeePerYear,

  @NotNull(message = "Degree languages required")
   List<Language> languages
) {}
