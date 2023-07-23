package com.leskiewicz.schoolsystem.course.dto;

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
public class CourseDto extends RepresentationModel<CourseDto> {

    @NonNull private final Long id;
    @NonNull private final String title;
    @NonNull private final int duration_in_hours;
}
