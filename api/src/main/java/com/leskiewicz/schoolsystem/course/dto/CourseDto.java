package com.leskiewicz.schoolsystem.course.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.course.CourseScope;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Getter
@Builder
@Relation(collectionRelation = "courses")
@AllArgsConstructor
public class CourseDto extends RepresentationModel<CourseDto> {

  @NonNull private final Long id;
  @NonNull private final String title;
  @NonNull private final int durationInHours;
  @NonNull private final String faculty;
  @NonNull private final String teacher;
  @NonNull private final Language language;
  @NonNull private final List<CourseScope> scope;

  @JsonIgnore @NotNull private final Long facultyId;
  @JsonIgnore @NotNull private final Long teacherId;
}
