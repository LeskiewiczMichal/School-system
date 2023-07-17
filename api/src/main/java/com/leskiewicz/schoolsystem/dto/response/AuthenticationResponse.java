package com.leskiewicz.schoolsystem.dto.response;

import com.leskiewicz.schoolsystem.dto.entity.UserDto;
import com.leskiewicz.schoolsystem.model.User;
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
