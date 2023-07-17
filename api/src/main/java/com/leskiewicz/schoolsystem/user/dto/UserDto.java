package com.leskiewicz.schoolsystem.user.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "users")
public class UserDto extends RepresentationModel<UserDto> {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String faculty;
    private String degree;
}
