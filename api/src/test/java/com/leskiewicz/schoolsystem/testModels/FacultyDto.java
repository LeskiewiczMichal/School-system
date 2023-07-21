package com.leskiewicz.schoolsystem.testModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Builder(toBuilder = true)
@Relation(collectionRelation = "faculties")
@AllArgsConstructor
public class FacultyDto
    extends RepresentationModel<com.leskiewicz.schoolsystem.faculty.dto.FacultyDto> {

  @NonNull private final Long id;

  @NonNull private final String name;
}
