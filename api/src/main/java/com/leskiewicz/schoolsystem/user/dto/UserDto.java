package com.leskiewicz.schoolsystem.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "users")
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

    public UserDto(
            @JsonProperty("id") Long id,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("faculty") String faculty,
            @JsonProperty("degree") String degree
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.faculty = faculty;
        this.degree = degree;
    }
}
