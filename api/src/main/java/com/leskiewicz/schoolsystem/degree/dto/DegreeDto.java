package com.leskiewicz.schoolsystem.degree.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "degrees")
@AllArgsConstructor
public class DegreeDto extends RepresentationModel<DegreeDto> {

  private final Long id;
  private final DegreeTitle title;
  private final String fieldOfStudy;


}
