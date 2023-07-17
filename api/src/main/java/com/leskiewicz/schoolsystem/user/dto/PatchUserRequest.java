package com.leskiewicz.schoolsystem.user.dto;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PatchUserRequest {

        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String facultyName;
        private String degreeField;
        private DegreeTitle degreeTitle;
}
