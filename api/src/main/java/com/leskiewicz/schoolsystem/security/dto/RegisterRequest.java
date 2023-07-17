package com.leskiewicz.schoolsystem.security.dto;


import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class RegisterRequest {

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

    @NotNull(message = "Degree field of study required")
    private String degreeField;

    @NotNull(message = "Degree title required")
    private DegreeTitle degreeTitle;
}
