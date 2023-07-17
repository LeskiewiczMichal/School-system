package com.leskiewicz.schoolsystem.security.dto;

import com.leskiewicz.schoolsystem.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse extends RepresentationModel<AuthenticationResponse> {

    private String token;
    private UserDto user;
}
