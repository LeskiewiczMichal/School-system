package com.leskiewicz.schoolsystem.user.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class PatchUserRequest {

  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String facultyName;
  private String degreeField;
  private DegreeTitle degreeTitle;
}
