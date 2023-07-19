package com.leskiewicz.schoolsystem.testModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder(toBuilder = true)
@Relation(collectionRelation = "users")
public class UserDto extends RepresentationModel<com.leskiewicz.schoolsystem.user.dto.UserDto> {

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
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