package com.leskiewicz.schoolsystem.faculty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "faculties")
@AllArgsConstructor
public class FacultyDto extends RepresentationModel<FacultyDto> {

  @NonNull private final Long id;

  @NonNull private final String name;
}
