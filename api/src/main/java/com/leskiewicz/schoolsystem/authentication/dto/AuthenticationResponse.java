package com.leskiewicz.schoolsystem.authentication.dto;

import com.leskiewicz.schoolsystem.user.dto.UserDto;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AuthenticationResponse extends RepresentationModel<AuthenticationResponse> {

  private String token;
  private UserDto user;
}
