package com.leskiewicz.schoolsystem.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@Relation(collectionRelation = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String faculty;
    private String degree;
}
