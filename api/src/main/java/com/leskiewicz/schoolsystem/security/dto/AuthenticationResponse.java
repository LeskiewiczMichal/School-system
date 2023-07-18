package com.leskiewicz.schoolsystem.security.dto;

import com.leskiewicz.schoolsystem.user.dto.UserDto;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@AllArgsConstructor
public class AuthenticationResponse extends RepresentationModel<AuthenticationResponse> {

    private String token;
    private UserDto user;
}
