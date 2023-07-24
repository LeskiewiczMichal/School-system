package com.leskiewicz.schoolsystem.testModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder(toBuilder = true)
@Relation(collectionRelation = "users")
@AllArgsConstructor
public class UserDto extends RepresentationModel<com.leskiewicz.schoolsystem.user.dto.UserDto> {

  @NonNull private final Long id;
  @NonNull private final String firstName;
  @NonNull private final String lastName;
  @NonNull private final String email;
  @NonNull private final String faculty;

  @JsonIgnore
  @NotNull
  private final Long facultyId;

  // Degree may be null if user is not a student
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final String degree;
  @JsonIgnore private final Long degreeId;
}