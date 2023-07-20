package com.leskiewicz.schoolsystem.faculty.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateFacultyRequest {

  @NotNull
  private String name;

}
