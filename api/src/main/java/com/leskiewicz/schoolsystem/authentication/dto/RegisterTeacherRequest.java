package com.leskiewicz.schoolsystem.authentication.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterTeacherRequest {

    @NotNull(message = "First name required")
    private String firstName;

    @NotNull(message = "Last name required")
    private String lastName;

    @NotNull(message = "Email required")
    private String email;

    @NotNull(message = "Password required")
    private String password;

    @NotNull(message = "Faculty name required")
    private String facultyName;

}
