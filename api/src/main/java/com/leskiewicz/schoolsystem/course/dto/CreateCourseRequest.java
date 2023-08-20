package com.leskiewicz.schoolsystem.course.dto;

import com.leskiewicz.schoolsystem.course.CourseScope;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

  @NotNull(message = "Language required")
  private final Language language;

  @NotNull(message = "Scope required")
  private final List<CourseScope> scope;

  @NotNull(message = "ECTS required")
  private final int ECTS;
}
