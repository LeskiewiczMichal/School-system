package com.leskiewicz.schoolsystem.degree.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Builder
@Relation(collectionRelation = "degrees")
@AllArgsConstructor
public class DegreeDto extends RepresentationModel<DegreeDto> {

  @NotNull private final Long id;
  @NotNull private final DegreeTitle title;
  @NotNull private final String fieldOfStudy;
  @NotNull private final String faculty;
  private String description;
  @JsonIgnore @NotNull private final Long facultyId;
}
