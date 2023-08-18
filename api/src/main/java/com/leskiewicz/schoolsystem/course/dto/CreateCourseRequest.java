package com.leskiewicz.schoolsystem.course.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateCourseRequest {

  @NotNull(message = "Course title required")
  private final String title;

  @NotNull(message = "Duration of course in hours required")
  private final int durationInHours;

  @NotNull(message = "FacultyId name required")
  private final Long facultyId;

  @NotNull(message = "TeacherId name required")
  private final Long teacherId;

  @NotNull(message = "Description required")
  private final String description;
}
