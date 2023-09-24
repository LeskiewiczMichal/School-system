package com.leskiewicz.schoolsystem.faculty.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record CreateFacultyRequest(
  @NotNull(message = "Faculty name required")
  String name
) {}
