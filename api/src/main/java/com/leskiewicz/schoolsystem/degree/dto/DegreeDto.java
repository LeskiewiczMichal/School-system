package com.leskiewicz.schoolsystem.degree.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

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
  @NotNull private final Double lengthOfStudy;
  @NotNull private String description;
  @NotNull private Double tuitionFeePerYear;
  @NotNull private List<Language> languages;
  private String imageName;
  @JsonIgnore @NotNull private final Long facultyId;
}
