package com.leskiewicz.schoolsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotNull(message = "Email required")
    private String email;

    @NotNull(message = "Password required")
    private String password;
}
