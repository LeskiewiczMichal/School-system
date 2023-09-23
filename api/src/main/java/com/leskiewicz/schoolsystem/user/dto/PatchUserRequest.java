package com.leskiewicz.schoolsystem.user.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import lombok.*;

@Builder
public record PatchUserRequest(String firstName, String lastName, String email, String password) {}
