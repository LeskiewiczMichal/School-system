package com.leskiewicz.schoolsystem.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "users")
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

    @NonNull
    private final Long id;
    @NonNull
    private final String firstName;
    @NonNull
    private final String lastName;
    @NonNull
    private final String email;
    @NonNull
    private final String faculty;
    private final String degree;
}
