package com.leskiewicz.schoolsystem.authentication;

import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationRequest;
import com.leskiewicz.schoolsystem.authentication.dto.AuthenticationResponse;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterRequest;
import com.leskiewicz.schoolsystem.authentication.dto.RegisterTeacherRequest;

public interface AuthenticationService {

  AuthenticationResponse register(RegisterRequest request);

  AuthenticationResponse authenticate(AuthenticationRequest request);

  AuthenticationResponse registerTeacherAccount(RegisterTeacherRequest request);

  AuthenticationResponse authenticateWithToken(String token);
}
