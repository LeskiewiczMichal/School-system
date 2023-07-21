package com.leskiewicz.schoolsystem.faculty.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFacultyRequest {

  @NotNull(message = "Faculty name required")
  private String name;
}
