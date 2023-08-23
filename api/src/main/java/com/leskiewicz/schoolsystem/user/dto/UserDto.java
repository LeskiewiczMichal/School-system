package com.leskiewicz.schoolsystem.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leskiewicz.schoolsystem.authentication.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "users")
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

  @NonNull private final Long id;
  @NonNull private final String firstName;
  @NonNull private final String lastName;
  @NonNull private final String email;
  @NonNull private final String faculty;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final Role role;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final String profilePictureName;

  // Needed to add link in UserDtoAssembler
  @JsonIgnore @NotNull private final Long facultyId;

  // Degree may be null if user is not a student
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final String degree;

  // Needed to add optional links in UserDtoAssembler
  @JsonIgnore private final Long degreeId;
  @JsonIgnore private final Long teacherDetailsId;
}
