package com.leskiewicz.schoolsystem.testModels;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder(toBuilder = true)
@Relation(collectionRelation = "degrees")
@AllArgsConstructor
public class DegreeDto extends RepresentationModel<com.leskiewicz.schoolsystem.degree.dto.DegreeDto> {

    @NotNull
    private final Long id;
    @NotNull private final DegreeTitle title;
    @NotNull private final String fieldOfStudy;
    @NotNull private final String faculty;
}
