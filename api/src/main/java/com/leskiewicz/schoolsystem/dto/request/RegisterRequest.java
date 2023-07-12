package com.leskiewicz.schoolsystem.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
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

}
